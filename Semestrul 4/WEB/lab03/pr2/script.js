document.addEventListener('DOMContentLoaded', function () {
    const submitBtn = document.getElementById('submitBtn');
    const messageDiv = document.getElementById('message');

    submitBtn.addEventListener('click', validateForm);

    function validateForm() {
        reset();

        const nameInput = document.getElementById('name');
        const birthdateInput = document.getElementById('birthdate');
        const ageInput = document.getElementById('age');
        const emailInput = document.getElementById('email');

        let isValid = true;
        let invalidFields = [];

        if (!nameInput.value.trim()) {
            makeInvalid(nameInput);
            invalidFields.push('nume');
            isValid = false;
        }

        if (!birthdateInput.value) {
            makeInvalid(birthdateInput);
            invalidFields.push('data nașterii');
            isValid = false;
        } else {
            const birthdate = new Date(birthdateInput.value);
            const today = new Date();
        }

        if (!ageInput.value) {
            makeInvalid(ageInput);
            invalidFields.push('vârsta');
            isValid = false;
        } else {
            const age = parseInt(ageInput.value);

            if (isNaN(age) || age < 0 || age > 120) {
                makeInvalid(ageInput);
                invalidFields.push('vârsta');
                isValid = false;
            }

            if (birthdateInput.value) {
                const birthYear = new Date(birthdateInput.value).getFullYear();
                const currentYear = new Date().getFullYear();
                const calculatedAge = currentYear - birthYear;

                if (Math.abs(calculatedAge - age) > 1) {
                    makeInvalid(ageInput);
                    makeInvalid(birthdateInput);
                    invalidFields.push('vârsta');
                    isValid = false;
                }
            }
        }

        if (!emailInput.value.trim()) {
            makeInvalid(emailInput);
            invalidFields.push('adresa de email');
            isValid = false;
        } else {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(emailInput.value)) {
                makeInvalid(emailInput);
                invalidFields.push('adresa de email');
                isValid = false;
            }
        }

        if (isValid) {
            displaySuccessMessage();
        } else {
            displayErrorMessage(invalidFields);
        }
    }

    function makeInvalid(element) {
        element.classList.add('error');
    }

    function reset() {
        const inputs = document.querySelectorAll('input');
        inputs.forEach(input => {
            input.classList.remove('error');
        });

        messageDiv.style.display = 'none';
        messageDiv.className = '';
    }

    function displaySuccessMessage() {
        messageDiv.textContent = 'Datele sunt completate corect';
        messageDiv.className = 'success-message';
        messageDiv.style.display = 'block';
    }

    function displayErrorMessage(invalidFields) {
        const filedsString = invalidFields.join(' si ');
        messageDiv.textContent = ''
        if (invalidFields.length === 1) {
            messageDiv.textContent = `Campul ${filedsString} nu este completat corect`;
        } else {
            messageDiv.textContent = `Campurile ${filedsString} nu sunt completate corect`;
        }
        messageDiv.className = 'error-message';
        messageDiv.style.display = 'block';
    }
});