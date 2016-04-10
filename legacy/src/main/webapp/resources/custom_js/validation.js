function Validation() {

}

Validation.validEmail = function (email) {
    var error = "";
    if (!email) {
        $('#form_group_email').removeClass('has-success').addClass('has-error');
        error += "Email is required! ";
    } else {
        var valid_email = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
        if (!email.match(valid_email)) {
            $('#form_group_email').removeClass('has-success').addClass("has-error");
            error += "Email is invalid! ";
        } else {
            $('#form_group_email').removeClass('has-error').addClass('has-success');
        }
    }
    return error;
};

Validation.validEmailRegistration = function (email) {
    var error = Validation.validEmail(email);
    if (error) {
        return error;
    } else {
      if(email.length > 50) {
          $('#form_group_email').removeClass('has-success').addClass('has-error');
          error += "Please, use email 50 symbols length! ";
      } else {
          $('#form_group_email').removeClass('has-error').addClass('has-success');
      }
    }
    return error;
};

Validation.validFullNameRegistration = function (fullName) {
    var error = "";
    if (!fullName) {
        $('#form_group_full_name').removeClass('has-success').addClass('has-error');
        error += "Full name is required! ";
    } else {
        if(fullName.length > 100) {
            $('#form_group_full_name').removeClass('has-success').addClass('has-error');
            error += "Please, shorten your full name to 100 symbols! ";
        } else {
            $('#form_group_full_name').removeClass('has-error').addClass('has-success');
        }
    }
    return error;
};

Validation.validPassword = function (password, length) {
    var error = "";
    if (!password) {
        $('#form_group_password').removeClass('has-success').addClass('has-error');
        error += "Password is required! ";
    } else {
        if(typeof(length)!= "undefined" && password.length < length) {
            $('#form_group_password').removeClass('has-success').addClass('has-error');
            error += "Password must be more than "+length+" symbols length! ";
        }
        $('#form_group_password').removeClass('has-error').addClass('has-success');
    }
    return error;
};

Validation.validProjectName = function (name) {
    var error = "";
    if (!name) {
        $('#form-group-name').removeClass('has-success').addClass('has-error');
        error += "Name is required! ";
    } else {
        if(name.length > 300) {
            $('#form-group-name').removeClass('has-success').addClass('has-error');
            error += "Please, shorten the name. Not more than 300 symbols is possible! ";
        } else {
            $('#form-group-name').removeClass('has-error').addClass('has-success');
        }
    }
    return error;
};

Validation.validParticipants = function () {
    var error = "";
    if (!$('#users-list').text()) {
        $('#form-group-users').removeClass('has-success').addClass('has-error');
        error += "At least one participant is required! ";
    } else {
        $('#form-group-users').removeClass('has-error').addClass('has-success');
    }
    return error;
};

Validation.validDescription = function () {
    $('#form-group-desc').removeClass('has-error').addClass('has-success');
    return "";
};

Validation.validStatusPriorityCategory = function () {
    $('#form-group-status, #form-group-priority, #form-group-category').removeClass('has-error').addClass('has-success');
    return "";
};

Validation.validVersion = function (version) {
    var error = "";
    if (!$.isNumeric(version) || parseFloat(version) < 1 || parseFloat(version) > 10) {
        $('#form-group-version').removeClass('has-success').addClass('has-error');
        error += "Version must be a float number from 1 to 10! ";
    } else {
        //hack
        var ver = parseFloat(version);
        $('#version').val(ver.toFixed(1));
        $('#form-group-version').removeClass('has-error').addClass('has-success');
    }
    return error;
};

Validation.validAssignee = function(assignee) {
    var error = "";
    if($('#assignee-field').text() == "") {
        $('#form-group-assignee').removeClass('has-success').addClass('has-error');
        error += "One assignee must be chosen! ";
    } else {
        $('#form-group-assignee').removeClass('has-error').addClass('has-success');
    }
    return error;
};





