$(document).ready(function () {

    var tempUserTypeaheadList;

    function validate(name) {
        var error = "";
        error += Validation.validProjectName(name);
        error += Validation.validParticipants();
        error += Validation.validProjectDescription();
        if (error) {
            $('#invalid-project-add').removeClass('non-visible').text(error);
            return false;
        } else {
            $('#invalid-project-add').addClass('non-visible');
            return true;
        }
    }

    $('#btn-create-project, #btn-edit-project').click(function () {
        if (this.id == 'btn-create-project') {
            $('#modal-name-project').text("Add");
        } else {
            var currentProjectIdHref = $('li.active').find('a').attr('href');
            var currentProjectId = currentProjectIdHref.substr(1, currentProjectIdHref.length);
            $('#modal-name-project').text("Edit");
            $.ajax({
                url: '/api/projects/' + currentProjectId + '/participants',
                success: function (data) {
                    $.each(data, function (i, item) {
                        $('#pr-users-list').append(
                            '<span class="label label-info display-user-label" data-id="' + item.id + '">' + item.fullName +
                            '<button type="button" class="close margin-close-btn" style="float: none; font-size: 16px">' +
                            '<span aria-hidden="true">&times;</span></button></span>'
                        );
                    });
                    $('#pr-id').val(currentProjectId);
                    $('#pr-name').val($('ul li.active').find('a').text());
                    $('#pr-desc').val($('#' + currentProjectId + '-pr-description').text());
                }
            });
        }
    });

    $('#btn-save-project').click(function () {
        var name = $.trim($('#pr-name').val());
        var desc = $.trim($('#pr-desc').val());
        var users = [];
        $('#pr-users-list').children('span').each(function () {
            users.push($(this).data('id'));
        });
        if (validate(name)) {

            var id = $('#pr-id').val();
            var project = {
                "id": id == "" ? null : id,
                "name": name,
                "description": desc,
                "userIds": users
            };

            var type =  id == "" ? 'POST' : 'PUT';

            $.ajax({
                type: type,
                url: '/api/projects',
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                },
                data: JSON.stringify(project),
                success: function () {
                    cleanModal();
                    $('#project-modal').modal('hide');
                    location.reload();
                },
                error: function (data) {
                    var error = data.error;
                    $('#invalid-project-add').removeClass('non-visible').text(error);
                }
            });
        }
    });

    $('#pr-user').typeahead({
        source: function (query, process) {
            return $.ajax({
                url: "/api/users",
                data: {name: query},
                dataType: 'json',
                success: function (result) {

                    tempUserTypeaheadList = result;

                    var existingUsers = [];
                    $('#pr-users-list').children('span').each(function () {
                        var name = $(this).text();
                        existingUsers.push(name.substring(0, name.length - 1));
                    });

                    var resultList = [];

                    jQuery.each(result, function (i, val) {
                        if ($.inArray(val.fullName, existingUsers) < 0) {
                            resultList.push(val.fullName);
                        }
                    });

                    return process(resultList);

                }
            });
        },

        updater: function (name) {
            var item = $.grep(tempUserTypeaheadList, function (e) {
                return e.fullName == name;
            });
            if (item.length == 0) {
                // return empty string to clear input
                return '';
            } else {
                $('#pr-users-list').append(
                    '<span class="label label-info display-user-label" data-id="' + item[0].id + '">' + item[0].fullName +
                    '<button type="button" class="close margin-close-btn" style="float: none; font-size: 16px">' +
                    '<span aria-hidden="true">&times;</span></button></span>'
                );
                // return empty string to clear input
                return '';
            }
        }
    });

    $('#search-pr').typeahead({
        source: function (query, process) {
            return $.ajax({
                url: "/api/projects",
                data: {name: query},
                dataType: 'json',
                success: function (result) {

                    tempUserTypeaheadList = result;

                    var existingProjects = [];
                    $('#search-pr').children('span').each(function () {
                        var name = $(this).text();
                        existingProjects.push(name.substring(0, name.length - 1));
                    });

                    var resultList = [];

                    jQuery.each(result, function (i, val) {
                        if ($.inArray(val.name, existingProjects) < 0) {
                            resultList.push(val.name);
                        }
                    });

                    return process(resultList);

                }
            });
        },

        updater: function (name) {
            var item = $.grep(tempUserTypeaheadList, function (e) {
                return e.name == name;
            });
            if (item.length == 0) {
                // return empty string to clear input
                return '';
            } else {

                // return empty string to clear input
                return item[0].name;
            }
        }
    });

    $("#pr-users-list").on('click', 'button.close', function (e) {
        $(e.currentTarget.parentElement).remove();
    });

    $('#btn-cancel-project, #btn-close-project').click(function () {
        cleanModal();
    });

    $('#btn-search-project').click(function() {
        $.ajax({
            url: '/api/projects/' + $('#search-pr').val(),
            success: function (data) {
                $('#form-group-search').removeClass('has-error').addClass('has-success');
                $('#search-error').addClass('non-visible').text('');
                window.location.href = '/projects/' + data.projectId + '/tickets';
            },
            error: function (data) {
                var response = data.responseJSON;
                $('#form-group-search').addClass('has-error');
                $('#search-error').removeClass('non-visible').text(response.error);
            }
        });
    });

    function cleanModal() {
        $('#pr-desc').val('');
        $('#pr-name').val('');
        $('#pr-user').val('');
        $('#pr-users-list').empty();
        $('#invalid-project-add').addClass('non-visible').empty();
        $('#form-group-pr-name, #form-pr-group-desc, #form-group-users').removeClass('has-error has-success');
        $('#btn-create-project').blur();
    }

});
