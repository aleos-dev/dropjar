package com.aleos.web.controller;

import com.aleos.dto.SignUpPayload;
import com.aleos.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.UUID;

/**
 * Controller responsible for handling user authentication-related requests.
 * This includes showing the sign-in form, displaying the sign-up form, processing sign-up requests,
 * and verifying account activation tokens.
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final String ACTIVATION_LINK_FORMAT = "/auth/activate?token=%s";

    private final AuthenticationService authenticationService;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;

    @Value("${registration.mail.from}")
    private String emailSender;


    /**
     * Handles GET requests to display the sign-in form for user authentication.
     *
     * @return the name of the view to render the sign-in form
     */
    @GetMapping("/sign-in")
    public String showSignInForm() {
        return "auth/sign-in";
    }

    /**
     * Handles GET requests to display the sign-up form for user registration.
     *
     * @param model the model object to which attributes are added for rendering the view
     * @return the name of the view to render the sign-up form
     */
    @GetMapping("/sign-up")
    public String showSignUpForm(Model model) {
        model.addAttribute("signUpPayload", SignUpPayload.empty());
        return "auth/sign-up";
    }

    /**
     * Processes the user sign-up request. If there are validation errors in the sign-up
     * payload, it returns the sign-up view with errors. Otherwise, it delegates the
     * sign-up process to the `authenticationService`, sending out a confirmation email
     * and redirecting to the sign-in page upon successful registration.
     *
     * @param payload the payload containing user registration information
     * @param bindingResult the binding result containing validation errors, if any
     * @param redirectAttributes attributes for a redirect scenario
     * @param req the HTTP servlet request
     * @return the name of the view to be rendered
     */
    @PostMapping("/sign-up")
    public String processSignUp(@ModelAttribute @Valid SignUpPayload payload,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest req) {
        if (bindingResult.hasErrors()) {
            return "auth/sign-up";
        }

        authenticationService.signUp(
                payload,
                getMessage("registration.mail.subject"),
                emailSender,
                generateActivationLink(req),
                passwordEncoder
        );
        redirectAttributes.addFlashAttribute("message", getMessage("auth.signup.success"));

        return "redirect:/auth/sign-in";
    }

    /**
     * Handles the activation of a user's account upon confirmation from an email link.
     * This method processes the verification token and redirects the user
     * after setting appropriate success messages.
     *
     * @param token the UUID of the verification token provided in the activation email
     * @param redirectAttributes contains attributes for the redirect scenario
     * @return the redirection path to the sign-in page
     */
    @GetMapping("/activate")
    public String verifyActivation(@RequestParam UUID token,
                                   RedirectAttributes redirectAttributes
    ) {
        String activationEmail = authenticationService.processVerification(token);
        redirectAttributes.addFlashAttribute("message", getMessage("auth.activate.success"));
        redirectAttributes.addFlashAttribute("email", activationEmail);

        return "redirect:/auth/sign-in";
    }

    private String generateActivationLink(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getServletPath(), ACTIVATION_LINK_FORMAT);
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }
}
