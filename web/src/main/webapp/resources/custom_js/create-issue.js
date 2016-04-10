var checkedId;

$(document).ready(function () {

    var tempUserTypeaheadList;

    $('#btn-create-issue').click(function () {
            $('#modal-name-issue').text("Add");
    });

    function validate(name, version, assignee) {
        var error = "";
        error += Validation.validIssueName(name);
        error += Validation.validAssignee(assignee);
        error += Validation.validVersion(version);
        error += Validation.validIssueDescription();
        error += Validation.validStatusPriorityCategory();
        if (error) {
            $('#invalid-issue-edit').removeClass('non-visible').text(error);
            return false;
        } else {
            $('#invalid-issue-edit').addClass('non-visible');
            return true;
        }
    }

    $('#btn-save-issue').click(function () {
        var name = $.trim($('#is-name').val());
        var desc = $.trim($('#is-desc').val());
        var version = $.trim($('#is-version').val());
        var status = $('#is-status').val();
        var priority = $('#is-priority').val();
        var category = $('#is-category').val();
        var assignee = {
            id: $('#assignee-field').children('span').data('id')
        };
        var project = {
            id: projectId
        };
        if (validate(name, version, assignee)) {
            var issue = {
                "id": checkedId == undefined ? null : checkedId,
                "name": name,
                "description": desc,
                "priority": priority,
                "status": status == undefined || status == "" ? null : status,
                "project": project,
                "version": version,
                "category": category,
                "assignee": assignee.id == undefined ? null : assignee
            };

            $.ajax({
                type: "PUT",
                url: '/issue',
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                },
                data: JSON.stringify(issue),
                success: function () {
                    cleanModal();
                    $('#issue-modal').modal('hide');
                    location.reload();
                },
                error: function (data) {
                    var errorText = data.error;
                    $('#invalid-issue-edit').removeClass('non-visible').text(errorText);
                }
            });
        }
    });

    $('#is-assignee').typeahead({
        source: function (query, process) {
            return $.ajax({
                url: "/project/" + projectId + "/participants",
                data: {"query": query},
                dataType: 'json',
                success: function (result) {

                    tempUserTypeaheadList = result;

                    var existingUsers = [];
                    $('#assignee-field').children('span').each(function () {
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
            if ($('#assignee-field').text() == "") {
                var item = $.grep(tempUserTypeaheadList, function (e) {
                    return e.fullName == name;
                });
                if (item.length == 0) {
                    // return empty string to clear input
                    return '';
                } else {
                    $('#assignee-field').append(
                        '<span class="label label-info display-user-label" data-id="' + item[0].id + '">' + item[0].fullName +
                        '<button type="button" class="close margin-close-btn" style="float: none; font-size: 16px">' +
                        '<span aria-hidden="true">&times;</span></button></span>'
                    );
                    // return empty string to clear input
                    return '';
                }
            }
        }
    });

    $('#btn-cancel-issue, #btn-close-issue').click(function () {
        cleanModal();
    });

    $("#assignee-field").on('click', 'button.close', function (e) {
        $(e.currentTarget.parentElement).remove();
    });

    function cleanModal() {
        $('#is-name').val('');
        $('#form-group-status').addClass('non-visible');
        $('#is-version').val('');
        $('#is-desc').val('');
        $('#is-assignee').val('');
        $('#assignee-field').empty();
        $('#invalid-issue-edit').addClass('non-visible').empty();
        $('#form-group-is-name, #form-group-is-desc, #form-group-status, #form-group-category, #form-group-priority, #form-group-assignee, #form-group-version').removeClass('has-error has-success');
    }


});

