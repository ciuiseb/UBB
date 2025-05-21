$(document).ready(function () {
    const $submitBtn = $('#submitBtn');
    const $messageDiv = $('#message');

    $submitBtn.on('click', validateForm);

    function validateForm() {
        reset();

        const $nameInput = $('#name');
        const $birthdateInput = $('#birthdate');
        const $ageInput = $('#age');
        const $emailInput = $('#email');

        let isValid = true;
        let invalidFields = [];

        if (!$nameInput.val().trim()) {
            makeInvalid($nameInput);
            invalidFields.push('nume');
            isValid = false;
        }

        if (!$birthdateInput.val()) {
            makeInvalid($birthdateInput);
            invalidFields.push('data nașterii');
            isValid = false;
        } else {
            const birthdate = new Date($birthdateInput.val());
            const today = new Date();
        }

        if (!$ageInput.val()) {
            makeInvalid($ageInput);
            invalidFields.push('vârsta');
            isValid = false;
        } else {
            const age = parseInt($ageInput.val());

            if (isNaN(age) || age < 0 || age > 120) {
                makeInvalid($ageInput);
                invalidFields.push('vârsta');
                isValid = false;
            }

            if ($birthdateInput.val()) {
                const birthYear = new Date($birthdateInput.val()).getFullYear();
                const currentYear = new Date().getFullYear();
                const calculatedAge = currentYear - birthYear;

                if (Math.abs(calculatedAge - age) > 1) {
                    makeInvalid($ageInput);
                    makeInvalid($birthdateInput);
                    invalidFields.push('vârsta');
                    isValid = false;
                }
            }
        }

        if (!$emailInput.val().trim()) {
            makeInvalid($emailInput);
            invalidFields.push('adresa de email');
            isValid = false;
        } else {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test($emailInput.val())) {
                makeInvalid($emailInput);
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

    function makeInvalid($element) {
        $element.addClass('error');
    }

    function reset() {
        $('input').removeClass('error');
        $messageDiv.hide().removeClass();
    }

    function displaySuccessMessage() {
        $messageDiv
            .text('Datele sunt completate corect')
            .addClass('success-message')
            .show();
    }

    function displayErrorMessage(invalidFields) {

        $messageDiv.empty().removeClass();
        switch (invalidFields.length) {
            case 1:
                $messageDiv.text(`Campul ${invalidFields[0]} nu este completat corect`);
                break;
            default:
                const fieldsString = invalidFields.join(' si ');
                $messageDiv.text(`Campurile ${fieldsString} nu sunt completate corect`);
                break;
        }
        $messageDiv
            .addClass('error-message')
            .show();
    }
});