document.addEventListener('DOMContentLoaded', function() {

    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const passwordHelp = document.getElementById('passwordHelp');

    // Password Strength Indicator and Validation
    passwordInput.addEventListener('input', validatePassword);
    confirmPasswordInput.addEventListener('input', validateConfirmPassword);

    function validatePassword() {
        const password = passwordInput.value;

        if(password.length < 3) {
            passwordHelp.textContent = 'Password is too short.';
            passwordHelp.style.color = 'red';
            passwordInput.setCustomValidity('Password is too short.');
        } else if(!/[A-Za-z]/.test(password) || !/[0-9]/.test(password)) {
            passwordHelp.textContent = 'Password must contain both letters and numbers.';
            passwordHelp.style.color = 'red';
            passwordInput.setCustomValidity('Password must contain both letters and numbers.');
        } else {
            passwordHelp.textContent = 'Strong password.';
            passwordHelp.style.color = 'green';
            passwordInput.setCustomValidity('');
        }

        // Re-validate confirm password in case the user changed the original password
        validateConfirmPassword();
    }

    function validateConfirmPassword() {
        if (passwordInput.value !== confirmPasswordInput.value) {
            confirmPasswordInput.setCustomValidity('Passwords do not match.');
        } else {
            confirmPasswordInput.setCustomValidity('');
        }
    }

    // Password Visibility Toggle
    const togglePasswordButton = document.getElementById('togglePassword');
    const togglePasswordIcon = document.getElementById('togglePasswordIcon');

    togglePasswordButton.addEventListener('click', function() {
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);

        // Toggle icon
        togglePasswordIcon.classList.toggle('bi-eye');
        togglePasswordIcon.classList.toggle('bi-eye-slash');
    });
});