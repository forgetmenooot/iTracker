$(document).ready(function () {

    $('#sign_up_btn').click(function () {
        this.blur();

        var email = $.trim($('#email').val());
        var password = $.trim($('#password').val());
        var full_name = $.trim($('#full_name').val());

        if (validate(email, password, full_name)) {
            var credentials = {
                "email": email,
                "password": password,
                "fullName": full_name
            };
            $.ajax({
                type: 'POST',
                url: '/api/auth/register',
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                },
                data: JSON.stringify(credentials),
                success: function (data) {
                    var success = data.success;
                    $('#alert-message').removeClass('non-visible').removeClass('has-danger').removeClass('alert-danger').addClass('alert-success').addClass('has-success').text(success);
                    $('#full_name').val('');
                    $('#email').val('');
                    $('#password').val('');
                },
                error: function (data) {
                    var error = data.responseJSON.error;
                    $('#password').val('');
                    $('#form_group_password').removeClass('has-success').addClass('has-error');
                    $('#form_group_full_name').removeClass('has-success').addClass('has-error');
                    $('#form_group_email').removeClass('has-success').addClass('has-error');
                    $('#alert-message').removeClass('non-visible').removeClass('has-success').removeClass('alert-success').addClass('alert-danger').addClass('has-error').text(error);
                }
            });
        }
        return false;
    });

    function validate(email, password, fullName) {
        var error = "";
        error += Validation.validPassword(password, 6);
        error += Validation.validEmailRegistration(email);
        error += Validation.validFullNameRegistration(fullName);
        if (error) {
            $('#alert-message').removeClass('non-visible').addClass('alert-danger').text(error);
            return false;
        } else {
            $('#alert-message').addClass('non-visible');
            return true;
        }
    }

});


