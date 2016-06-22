var issueId;
var projectId;
$.fn.editable.defaults.mode = 'popup';
var preLoaded = preLoad();
var issueLoaded;
var stompClient = null;
var lang = 'en-us';
var stompClient2 = null;
var stompClient3 = null;
var uploadCounter = 0;

function preLoad() {
    projectId = url('2');
    issueId = url('4');

    var userId = $('#user-session-id').val();
    return $.ajax({
        url: "/api/projects/" + projectId + "/check/" + userId
    });
}

function colorPriority(data) {
    if (data == 'CRITICAL') {
        return '<div class="color-critical">' + data + '</div>';
    } else if (data == 'BLOCKER') {
        return '<div class="color-blocker">' + data + '</div>';
    } else if (data == 'MAJOR') {
        return '<div class="color-major">' + data + '</div>';
    } else if (data == 'TRIVIAL') {
        return '<div class="color-trivial">' + data + '</div>';
    } else {
        return '<div class="color-enhancement">' + data + '</div>';
    }
}

$.when(preLoaded).done(function (data) {

    Hyphenator.run();

    $.ajax({
        url: "/api/projects/" + projectId + "/tickets/" + issueId,
        success: function (issue) {
            issueLoaded = issue;
            $('#issue-name').append(issue.name);
            $('#issue-info').append(
                '<table class="table table-striped table-hover" style="padding-left:50px;">' +
                '<tr><th>Project name:</th><td>' + Hyphenator.hyphenate(issue.project.name , lang) + '</td></tr>' +
                '<tr><th>Creation date:</th><td>' + moment(issue.creationDate).calendar() + '</td></tr>' +
                '<tr><th>Last update date:</th><td>' + (issue.lastUpdateDate == undefined ? "-" : moment(issue.lastUpdateDate).calendar()) + '</td></tr>' +
                '<tr><th>Category:</th><td>' + issue.category + '</td></tr>' +
                '<tr><th>Status:</th><td>' + issue.status + '</td></tr>' +
                '<tr><th>Priority:</th><td>' + colorPriority(issue.priority) + '</td></tr>' +
                '<tr><th>Creator:</th><td>' + Hyphenator.hyphenate(issue.creator.fullName , lang) + '</td></tr>' +
                '<tr><th>Assignee:</th><td>' + Hyphenator.hyphenate(issue.assignee.fullName , lang) + '</td></tr>' +
                '<tr><th>Description:</th><td><div style="max-height: 80px; overflow: auto;">' + (issue.description == undefined || issue.description == "" ? "-" : issue.description) + '</div></td></tr>' +
                '</table>'
            );

            if (!jQuery.isEmptyObject(issue.comments)) {
                $.each(issue.comments, function (i, comment) {
                    var first = '<li>' +
                        '<div  class="chat-body">' +
                        '<div class="header">' +
                        '<small class="pull-right text-muted" id="date_' + comment.id + '">' +
                        '<i class="fa fa-clock-o fa-fw"></i>'
                        + (comment.updateDate == undefined ? moment(comment.date).calendar() : moment(comment.updateDate).calendar()) +
                        '</small>' +
                        '<strong class="pull-left primary-font">' + comment.sender.fullName + '</strong>' +
                        '</div>' +
                        '<div style="padding-top: 6%">';
                    if (comment.sender.id == $('#user-session-id').val()) {
                        var second =
                            '<a  class="comment" data-type="textarea" data-pk="' + comment.id + '">' +
                            Hyphenator.hyphenate(comment.comment, lang) +
                            '</a>' +
                            '<a  class="close del-comment"  data-value="Are you sure you want to delete comment?" data-pk="' + comment.id + '"><span aria-hidden="true">&times;</span>' +
                            '</a>' +
                            '</div>' +
                            '</div>' +
                            '</li>';
                    } else {
                        var second =
                            '<p>' +
                            Hyphenator.hyphenate(comment.comment, lang) +
                            '</p>' +
                            '</div>' +
                            '</div>' +
                            '</li>';
                    }
                    $('#issue-comments').append(first + second);
                });
            } else {
                $('#issue-comments').append(
                    '<li class="text-center no-comment">No comments found!</li>'
                );
            }

            // Show attachments
            $('div#files').empty();
            // if key exists -> show file list
            if (!$.isEmptyObject(issue.attachmentPaths)) {
                $.each(issue.attachmentPaths, function (index, value) {
                    var name = value.split("/")[6];
                    var href = "/api/projects/" + projectId + "/tickets/" + issueId + '/files/' + name;
                    var deleteHref = "/api/projects/" + projectId + "/tickets/" + issueId + '/files/' + name;
                    $('div#files').append(
                        '<div class="attachment-table">' +
                        '<a href="' + href + '" target="_blank">' + (name) + '</a>' +
                        '<span class="remove-attachment close" data-href="' + deleteHref + '">  &times;</span>' +
                        '</div>');
                });
            } else {
                $('div#files').append('<span>' + '-' + '</span>');
            }

            animateScroll();

            connect();

            initAttachmentEvents();

            //Update
            $('.comment').editable({
                send: 'always',
                url: function (params) {
                    var comment = params.value;
                    var commentId = params.pk;

                    stompClient2.send("/app/commenthandlerupdate", {}, JSON.stringify(
                        {
                            "commentId": commentId,
                            "comment": comment,
                            "issueId": issueId,
                            "userId": $('#user-session-id').val()
                        }));

                },
                validate: function (v) {
                    if (!$.trim(v)) return 'Comment is required!';
                    else if ($.trim(v).length > 500) return 'Please, shorten your comment. Not more than 500 symbols is possible!';
                }
            });

            //Remove
            $('.del-comment').editable({
                send: 'always',
                savenochange: true,
                url: function (params) {
                    var commentId = params.pk;

                    stompClient3.send("/app/commenthandlerremove", {}, JSON.stringify(
                        {
                            "commentId": commentId,
                            "comment": null,
                            "issueId": issueId,
                            "userId": null
                        }));

                },
                tpl: "<input type='text' style='width: 280px' readonly>"
            });
        }
    });


    function connect() {
        var socket = new SockJS('/commenthandleradd');

        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/add', function (calResult) {
                showMessage(JSON.parse(calResult.body));
            });
            stompClient.subscribe('/user/errors', function (calResult) {
                showError(calResult.body);
            });
        });

        var socket2 = new SockJS('/commenthandlerupdate');

        stompClient2 = Stomp.over(socket2);
        stompClient2.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient2.subscribe('/edit', function (calResult) {
                showMessage2(JSON.parse(calResult.body));
            });
        });

        var socket3 = new SockJS('/commenthandlerremove');

        stompClient3 = Stomp.over(socket3);
        stompClient3.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient3.subscribe('/remove', function (calResult) {
                showMessage3(JSON.parse(calResult.body));
            });
        });
    }

    $('#btn-chat-send').on('click', function () {
        //obtain massage
        if ($('#issue-comments').children().first().hasClass('no-comment')) {
            $('#issue-comments').children().first().addClass('non-visible');
        }

        var message = $.trim($('#message').val());

        if (!message) {
            //add red border to message input and show error messageto show that the message can't be empty
            $('#form-group-message').addClass("has-error");
            $('#invalid-input').text("Comment is required!");
        } else if (message.length > 500) {
            $('#form-group-message').addClass("has-error");
            $('#invalid-input').text("Please, shorten your comment. Not more than 500 symbols is possible!");
        } else {
            //clean input and error
            $('#form-group-message').removeClass("has-error");
            $('#invalid-input').text("");

            stompClient.send("/app/commenthandleradd", {}, JSON.stringify(
                {
                    "comment": message,
                    "issueId": issueId,
                    "userId": $('#user-session-id').val(),
                    "commentId": null
                }));

            //unfocus send-message button
            $('#btn-chat-send').blur();
        }
    });

    //handle sending message on "Enter" button
    $('body').keypress(function (eventCode) {
        if (eventCode.keyCode == 13) {
            $('#btn-chat-send').click();
        }
    });

    function showMessage(comment) {

        var first = '<li>' +
            '<div  class="chat-body">' +
            '<div class="header">' +
            '<small class="pull-right text-muted" id="date_' + comment.id + '">' +
            '<i class="fa fa-clock-o fa-fw"></i>'
            + (comment.updateDate == undefined ? moment(comment.date).calendar() : moment(comment.updateDate).calendar()) +
            '</small>' +
            '<strong class="pull-left primary-font">' + comment.sender.fullName + '</strong>' +
            '</div>' +
            '<div style="padding-top: 6%">';
        if (comment.sender.id == $('#user-session-id').val()) {
            var second =
                '<a  class="comment" data-type="textarea" data-pk="' + comment.id + '">' +
                Hyphenator.hyphenate(comment.comment, lang) +
                '</a>' +
                '<a  class="close del-comment"  data-value="Are you sure you want to delete comment?" data-pk="' + comment.id + '"><span aria-hidden="true">&times;</span>' +
                '</a>' +
                '</div>' +
                '</div>' +
                '</li>';
        } else {
            var second =
                '<p>' +
                Hyphenator.hyphenate(comment.comment, lang) +
                '</p>' +
                '</div>' +
                '</div>' +
                '</li>';
        }
        $('#issue-comments').append(first + second);

        var messageInput = $('#message');

        //clean message input from text
        messageInput.val('');

        //focus to message input
        messageInput.focus();

        animateScroll();

        //clean message input from errors
        $('#form-group-message').removeClass("has-error");
        $('#invalid-input').text('');

        //Update
        $('.comment').editable({
            send: 'always',
            url: function (params) {
                var comment = params.value;
                var commentId = params.pk;

                stompClient2.send("/app/commenthandlerupdate", {}, JSON.stringify(
                    {
                        "commentId": commentId,
                        "comment": comment,
                        "issueId": issueId,
                        "userId": $('#user-session-id').val()
                    }));

            },
            validate: function (v) {
                if (!$.trim(v)) return 'Comment is required!';
                else if ($.trim(v).length > 500) return 'Please, shorten your comment. Not more than 500 symbols is possible!';
            }
        });

        //Remove
        $('.close').editable({
            send: 'always',
            savenochange: true,
            url: function (params) {
                var commentId = params.pk;

                stompClient3.send("/app/commenthandlerremove", {}, JSON.stringify(
                    {
                        "commentId": commentId,
                        "comment": null,
                        "issueId": issueId,
                        "userId": null
                    }));

            },
            tpl: "<input type='text' style='width: 280px' readonly>"
        });

    }

    function showMessage2(comment) {
        $('#date_' + comment.id).html('<i class="fa fa-clock-o fa-fw"></i>' + moment(comment.updateDate).calendar());
        $('#date_' + comment.id).parent().parent().find('a').attr('data-pk', comment.id)
    }

    function showMessage3(comment) {
        $('#date_' + comment.id).parent().parent().parent().addClass('non-visible');
    }

    //animate scrolling list of messages to the end of panel (last message)
    function animateScroll() {
        var messagesPanel = $('.chat-panel .panel-body');
        messagesPanel.animate({scrollTop: messagesPanel.prop("scrollHeight")}, 500);
    }

    function showError(error) {
        $('#invalid-input').text(error);
    }

    function initAttachmentEvents() {
        // Add filename near browse button to see attachment name
        $('.btn-file :file').unbind('click').on('change', function () {
            var file = $(this);
            var label = file.val().replace(/\\/g, '/').replace(/.*\//, '');
            var input = $(this).parents('.input-group').find('.filename');
            input.text(label);
        });

        // Delete field for extra attachment
        $('.remove-extra-file').unbind('click').on('click', function () {
            var id = $(this).attr('data-id');
            var selector = '#upload-' + id;
            $(selector).remove();
        });

        // Delete attachment
        $('.remove-attachment').unbind('click').on('click', function () {
            var href = $(this).attr('data-href');
            $('#btn-del-att').attr('data-href', href);
            $('#modalDeleteAtt').modal('show');
        });

        $('#btn-del-att').unbind('click').on('click', function () {
            var href = $(this).attr('data-href');
            $.ajax({
                type: "DELETE",
                url: href,
                success: function () {
                    $('#modalDeleteAtt').modal('hide');
                    location.reload();
                }
            });
        });
    }

    // init upload buttons
    var fileTable = $('#fileTable');
    fileTable.empty();
    fileTable.append('<div id="upload-0" class="input-group">' +
        '<span class="btn btn-sm btn-file">' +
        'Browse&hellip;' + '<input name="files[0]" type="file">' +
        '</span>' +
        '<span class="filename"></span>' +
        '<span data-id="0" class="remove-extra-file close">' + '    &times;' + '</span>' +
        '</div>'
    );

    // Append new input for uploading
    $('#addFile').unbind('click').on('click', function () {
        var fileTable = $('#fileTable');
        uploadCounter = uploadCounter + 1;
        fileTable.append(
            '<div id="upload-' + uploadCounter + '" class="input-group">' +
            '<span class="btn btn-sm btn-file">   ' +
            '   Browse&hellip; <input name="files[' + uploadCounter + ']" type="file">' +
            '</span>' +
            '<span class="filename"></span>' +
            '<span data-id="' + uploadCounter + '" class="remove-extra-file close">   &times;</span>' +
            '</div>');
        initAttachmentEvents();
    });

    // Get all files and upload using AJAX
    $('#uploadFile').unbind('click').on('click', function () {
        var formData = new FormData();
        $('input[type=file]').each(function (i, file) {
            formData.append('files[]', file.files[0]);
        });
        $.ajax({
            type: "POST",
            url: '/api/projects/' + projectId + '/tickets/' + issueId + '/files',
            cache: false,
            contentType: false,
            processData: false,
            data: formData,
            success: function () {
                location.reload();
            }, error: function (data) {
                if (data.hasOwnProperty('responseJSON') && !$.isEmptyObject(data.responseJSON)) {
                    var error = data.responseJSON;
                    var errorText = error.error;
                    $('#error').removeClass('non-visible').text(errorText);
                } else {
                    $('#error').text(data);
                }
            }
        });
    });
});


