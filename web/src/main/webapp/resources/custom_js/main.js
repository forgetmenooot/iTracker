function preLoadProjectNames() {
    var userId = $("#user-session-id").val();
    $.ajax({
        url: "/user/" + userId + "/projects",
        success: function (data) {
            if (!data.message_projects) {
                $.each(data, function (i, pr) {
                    $('#project-names').append(
                        '<li><a href="#' + pr.id + '" data-toggle="tab">' + pr.name + '</a></li>'
                    );
                    $('#pr-content').append(
                        '<div class="tab-pane fade in" id="' + pr.id + '">' +
                        '<span class="pull-right text-muted small" id="' + pr.id + '-pr-date"></span><br/>'+
                        '<div class="row">' +
                        '<div class="col-lg-3 col-md-6">' +
                        '<div class="panel panel-primary">' +
                        '<div class="panel-heading">' +
                        '<div class="row">' +
                        '<div class="col-xs-3">' +
                        '<i class="fa fa-envelope fa-3x"></i>' +
                        '</div>' +
                        '<div class="col-xs-9 text-right">' +
                        '<div class="huge" id="' + pr.id + '-opened-count"></div>' +
                        '<div>OPENED</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<a href="/project/issues?status=OPENED">' +
                        '<div class="panel-footer">' +
                        '<span class="pull-left">View Details</span>' +
                        '<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>' +
                        '<div class="clearfix"></div>' +
                        '</div>' +
                        '</a>' +
                        '</div>' +
                        '</div>' +
                        '<div class="col-lg-3 col-md-6">' +
                        '<div class="panel panel-primary">' +
                        '<div class="panel-heading">' +
                        '<div class="row">' +
                        '<div class="col-xs-3">' +
                        '<i class="fa fa-tasks fa-3x"></i>' +
                        '</div>' +
                        '<div class="col-xs-9 text-right">' +
                        '<div class="huge" id="' + pr.id + '-in-progress-count"></div>' +
                        '<div>IN PROGRESS</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<a href="/project/issues?status=INPROGRESS">' +
                        '<div class="panel-footer">' +
                        '<span class="pull-left">View Details</span>' +
                        '<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>' +
                        '<div class="clearfix"></div>' +
                        '</div>' +
                        '</a>' +
                        '</div>' +
                        '</div>' +
                        '<div class="col-lg-3 col-md-6">' +
                        '<div class="panel panel-primary">' +
                        '<div class="panel-heading">' +
                        '<div class="row">' +
                        '<div class="col-xs-3">' +
                        '<i class="fa fa-wrench fa-3x"></i>' +
                        '</div>' +
                        '<div class="col-xs-9 text-right">' +
                        '<div class="huge" id="' + pr.id + '-resolved-count"></div>' +
                        '<div>RESOLVED</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<a href="/project/issues?status=RESOLVED">' +
                        '<div class="panel-footer">' +
                        '<span class="pull-left">View Details</span>' +
                        '<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>' +
                        '<div class="clearfix"></div>' +
                        '</div>' +
                        '</a>' +
                        '</div>' +
                        '</div>' +
                        '<div class="col-lg-3 col-md-6">' +
                        '<div class="panel panel-primary">' +
                        '<div class="panel-heading">' +
                        '<div class="row">' +
                        '<div class="col-xs-3">' +
                        '<i class="fa fa-check-square-o fa-3x"></i>' +
                        '</div>' +
                        '<div class="col-xs-9 text-right">' +
                        '<div class="huge" id="' + pr.id + '-closed-count"></div>' +
                        '<div>CLOSED</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<a href="/project/issues?status=CLOSED">' +
                        '<div class="panel-footer">' +
                        '<span class="pull-left">View Details</span>' +
                        '<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>' +
                        '<div class="clearfix"></div>' +
                        '</div>' +
                        '</a>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<div class="row">' +
                        '<div class="col-lg-6 col-md-12">' +
                        '<div class="panel panel-primary">' +
                        '<div class="panel-heading">' +
                        '<i class="fa fa-male fa-fw"></i>Participants' +
                        '</div>' +
                        '<div class="panel-body">' +
                        '<div class="table-responsive" style="overflow-y: scroll; max-height: 400px">' +
                        '<table class="table table-striped table-hover">' +
                        '<thead>' +
                        '<tr>' +
                        '<th class="text-center">#</th>' +
                        '<th class="text-center">Full name</th>' +
                        '<th class="text-center">Email</th>' +
                        '</tr>' +
                        '</thead>' +
                        '<tbody id="' + pr.id + '-pr-participants"></tbody>' +
                        '</table>' +
                            //'<a href="#" class="btn btn-default btn-block">View all participants</a>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<div class="col-lg-6 col-md-12">' +
                        '<div class="panel panel-primary">' +
                        '<div class="panel-heading">' +
                        '<i class="fa fa-list-alt fa-fw"></i> My tickets in progress' +
                        '</div>' +
                        '<div class="panel-body">' +
                        '<div class="table-responsive" style="overflow-y: scroll; max-height: 400px">' +
                        '<table class="table table-striped table-hover">' +
                        '<thead>' +
                        '<tr>' +
                        '<th class="text-center">#</th>' +
                        '<th class="text-center">Name</th>' +
                        '<th class="text-center">Priority</th>' +
                        '<th class="text-center">Link</th>' +
                        '</tr>' +
                        '</thead>' +
                        '<tbody id="' + pr.id + '-issues-status"></tbody>' +
                        '</table>' +
                            //'<a href="#" class="btn btn-default btn-block">View all tasks</a>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<div class="row">' +
                        '<div class="col-lg-12 col-md-24">' +
                        '<div class="panel panel-primary">' +
                        '<div class="panel-heading">' +
                        '<i class="fa fa-pencil-square-o fa-fw"></i>Description' +
                        '</div>' +
                        '<div style="word-wrap: break-word; overflow-y: scroll; max-height: 400px" class="panel-body" id="' + pr.id + '-pr-description"></div>' +
                        '</div>' +
                        '</div>' +
                        '</div>'
                    );

                    $('#' + pr.id + '-pr-date').text(moment(pr.date).calendar());
                    $('#' + pr.id + '-pr-description').text(pr.description);

                    $.ajax({
                        url: "/project/" + pr.id + "/issues/count?status=OPENED",
                        success: function (data) {
                            $('#' + pr.id + '-opened-count').text(data);
                        }
                    });

                    $.ajax({
                        url: "/project/" + pr.id + "/issues/count?status=INPROGRESS",
                        success: function (data) {
                            $('#' + pr.id + '-in-progress-count').text(data);
                        }
                    });

                    $.ajax({
                        url: "/project/" + pr.id + "/issues/count?status=RESOLVED",
                        success: function (data) {
                            $('#' + pr.id + '-resolved-count').text(data);
                        }
                    });

                    $.ajax({
                        url: "/project/" + pr.id + "/issues/count?status=CLOSED",
                        success: function (data) {
                            $('#' + pr.id + '-closed-count').text(data);
                        }
                    });

                    $.ajax({
                        url: "/project/" + pr.id + "/participants",
                        success: function (data) {
                            $.each(data, function (i, user) {
                                $('#' + pr.id + '-pr-participants').append(
                                    '<tr>' +
                                    '<td class="text-center">' + (i + 1) + '</td>' +
                                    '<td style="word-wrap: break-word" class="text-center">' + user.fullName + '</td>' +
                                    '<td style="word-wrap: break-word" class="text-center">' + user.email + '</td>' +
                                    '</tr>'
                                );
                            });
                        }
                    });

                    $.ajax({
                        url: "/project/" + pr.id + "/issues?status=INPROGRESS&assignee_id=" + userId,
                        success: function (data) {
                            if (!data.message_status) {
                                $.each(data, function (i, issue) {
                                    $('#' + pr.id + '-issues-status').append(
                                        '<tr>' +
                                        '<td class="text-center">' + (i + 1) + '</td>' +
                                        '<td class="non-visible issue-id">' + issue.id + '</td>' +
                                        '<td style="word-wrap: break-word" class="text-center">' + issue.name + '</td>' +
                                        '<td style="word-wrap: break-word" class="text-center">' + issue.priority + '</td>' +
                                        '<td><a class="issue-link" href="/project/issue"><span class="pull-right" style="margin-top: 5%; margin-right: 40%"><i class="fa fa-arrow-circle-right"></i></span></a></td>' +
                                        '</tr>'
                                    );
                                });

                                $('.issue-link').on("click", function (e) {
                                    e.preventDefault();
                                    console.log($(this).parent().parent().find('.issue-id').text());
                                    Cookies.set("issueId", $(this).parent().parent().find('.issue-id').text());
                                    window.location.href = "/project/issue";
                                });

                            } else {
                                $('#' + pr.id + '-issues-status').parent().find('thead').addClass('non-visible');
                                $('#' + pr.id + '-issues-status').append(
                                    '<li class="list-group-item center-text">' +
                                    data.message_status +
                                    '</li>'
                                );
                            }
                        }
                    });

                });
                $('#project-names').find('li').first().addClass('active');
                $('#pr-content').find('div').first().addClass('active');
                projectId = $('#project-names').find('li.active > a').attr('href').substring(1);
                Cookies.set("projectId", projectId);

                $('#btn-archive-project').removeClass('disabled');
                $('#btn-edit-project').removeClass('disabled');
                $('#btn-create-issue').removeClass('disabled');

            } else {
                $('#project-names').append(
                    '<li class="center-text">' + data.message_projects + '</li>'
                );
                $('#btn-archive-project').addClass('disabled');
                $('#btn-edit-project').addClass('disabled');
                $('#btn-create-issue').addClass('disabled');
            }
            $(".nav-pills > li > a").click(function (e) {
                projectId = $(e.target).attr("href").substr(1, (e.target).length);
                Cookies.set("projectId", projectId);
            });

        }
    });
}

var projectId;

function cleanModal() {
    $('#invalid-project-archive').addClass('non-visible').empty();
}

$(document).ready(function () {

    preLoadProjectNames();

    $('#btn-arch-project').click(function () {
        var currentProjectIdHref = $('li.active').find('a').attr('href');
        var currentProjectId = currentProjectIdHref.substr(1, currentProjectIdHref.length);
        $.ajax({
            type: "POST",
            url: '/project/' + currentProjectId + '/archive',
            success: function () {
                $('#archive-project-modal').modal('hide');
            },
            error: function (data) {
                var error = data.error;
                $('#invalid-project-archive').removeClass('non-visible').text(error);
            }
        });
    });

    $('#btn-cancel-arch-project, #btn-close-arch-project').click(function () {
        cleanModal();
    });

});

