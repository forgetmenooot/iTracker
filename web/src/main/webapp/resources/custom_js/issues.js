var dt;
var projectId = Cookies.get("projectId");
// Array to track the ids of the details displayed rows
var detailRows = [];
var lang = 'en-us';
var isChecked;
var checkedId;
var status;
var preLoaded = preLoad();
var url;

function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
}

function preLoad() {
    var href = window.location.href,
        newUrl = href.substring(0, href.indexOf('?'));

    var status = getUrlParameter("status");
    //projectId = getUrlParameter("projectId");

    url = status == undefined || status == "" ? "/project/" + projectId + "/issues" : "/project/" + projectId + "/issues?status=" + status;
    window.history.replaceState({}, '', newUrl);

    return $.ajax({
        url: "/project/check?userId=" + $('#user-session-id').val() + "&projectId=" + projectId
    });
}

function format(d) {
    return '<table class="table table-striped table-hover" style="padding-left:50px;">' +
    '<tr>' +
    '<th>Name:</th>' +
    '<td>' + d.name + '</td>' +
    '</tr>' +
    '<tr>' +
    '<tr>' +
    '<th>Creation date:</th>' +
    '<td>' + moment(d.creationDate).calendar() + '</td>' +
    '</tr>' +
    '<tr>' +
    '<tr>' +
    '<th>Last update date:</th>' +
    '<td>' + (d.lastUpdateDate == undefined ? "-" : moment(d.lastUpdateDate).calendar()) + '</td>' +
    '</tr>' +
    '<tr>' +
    '<tr>' +
    '<th>Creator:</th>' +
    '<td>' + Hyphenator.hyphenate(d.creator.fullName, lang) + '</td>' +
    '</tr>' +
    '<tr>' +
    '<tr>' +
    '<th>Assignee:</th>' +
    '<td>' + Hyphenator.hyphenate(d.assignee.fullName, lang) + '</td>' +
    '</tr>' +
    '<tr>' +
    '<tr>' +
    '<th>Description:</th>' +
    '<td>' + Hyphenator.hyphenate(d.description, lang) + '</td>' +
    '</tr>' +
    '</table>';
}


function renderTable() {
    dt = $('#issues-table')
        .DataTable({
            "bInfo": false,
            rowId: 'id',
            ajax: {
                contentType: "application/json",
                dataType: 'json',
                url: url,
                type: "get",
                dataSrc: ''
            },
            columns: [
                {
                    title: "Details",
                    class: "details-control",
                    data: null,
                    defaultContent: "",
                    orderable: false
                },
                {
                    title: "ID",
                    data: "id",
                    render: function idFormatter(data) {
                        return data;
                    },
                    visible: false
                },
                {
                    title: "Name",
                    data: "name",
                    render: function nameFormatter(data) {
                        if (data.length < 10) {
                            return data;
                        }
                        var desc = data.substring(0, 10);
                        return desc.concat('...');
                    },
                    class: 'text-center'
                },
                {
                    title: "Category",
                    data: "category",
                    class: 'text-center'
                },
                {
                    title: "Priority",
                    data: "priority",
                    class: 'text-center',
                    render: function priorityFormatter(data) {
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
                },
                {
                    title: "Status",
                    data: "status",
                    class: 'text-center'
                },
                {
                    title: "Creator",
                    data: "creator.fullName",
                    render: function creatorFormatter(data) {
                        if (data.length < 10) {
                            return data;
                        }
                        var desc = data.substring(0, 10);
                        return desc.concat('...');
                    },
                    class: 'text-center'
                },
                {
                    title: "Assignee",
                    data: "assignee.fullName",
                    render: function assigneeFormatter(data) {
                        if (data.length < 10) {
                            return data;
                        }
                        var desc = data.substring(0, 10);
                        return desc.concat('...');
                    },
                    class: 'text-center'
                },
                {
                    title: "Creation date",
                    data: "creationDate",
                    render: function dateFormatter(data) {
                        if (data == null) {
                            return "-";
                        }
                        var date = moment(data).calendar().toString().substring(0, 10);
                        return date.concat('...');
                    },
                    class: 'text-center'
                },
                {
                    title: "Last update date",
                    data: "lastUpdateDate",
                    render: function dateFormatter(data) {
                        if (data == null) {
                            return "-";
                        }
                        var date = new Date(data).toString().substring(0, 10);
                        return date.concat('...');
                    },
                    class: 'text-center'
                },
                {
                    title: "Version",
                    data: "version",
                    class: 'text-center'
                },
                {
                    title: "Description",
                    data: "description",
                    class: 'text-center',
                    visible: false
                }
            ],
            paging: true,
            "pagingType": "simple_numbers",
            //scrollY: 385,
            "initComplete": function () {
                cssDocument();
            }
        });
}

function cssDocument() {
    // add placeholder to searchbox
    $('.dataTables_filter label input').attr('placeholder', 'Search').removeClass('input-sm');
    // remove label near search
    $('.dataTables_filter label').get(0).childNodes[0].nodeValue = '';

    //$('div.dataTables_filter').parent().first().css('margin-top', '1.5%');

    //$('div.dataTables_filter').parent().parent().children().first().append('<h3><span class="label label-default">' + dt.data()[0].project.name + '</span></h3>');

    if(preLoaded) {
        $('<div class="btn-group" role="group" style="margin-left: 2%">' +
            '<button type="button" class="btn btn-primary inline" id="btn-is-edit" data-toggle="modal"  data-target="#issue-modal">Create</button>' +
            '<button type="button" class="btn btn-primary non-visible" data-toggle="modal"  id="btn-is-delete" data-target="#issue-modal-delete">Delete</button>' +
            '</div>').appendTo('div.dataTables_filter');

        $('#btn-is-edit').click(function () {
            if (this.textContent == 'Edit') {
                $('#modal-name-issue').text("Edit");
                $.ajax({
                    url: "/issue/" + checkedId,
                    success: function (data) {
                        $('#is-name').val(data.name);
                        $('#is-category').val(data.category);
                        $('#form-group-status').removeClass('non-visible');
                        $('#is-status').val(data.status);
                        $('#is-priority').val(data.priority);
                        $('#is-version').val(data.version);
                        $('#is-desc').val(data.description);
                        $('#assignee-field').append(
                            '<span class="label label-info display-user-label" data-id="' + data.assignee.id + '">' + data.assignee.fullName +
                            '<button type="button" class="close margin-close-btn" style="float: none; font-size: 16px">' +
                            '<span aria-hidden="true">&times;</span></button></span>'
                        );
                    }
                });
            } else {
                $('#modal-name-issue').text("Add");
            }
        });
    }

}

$.when(preLoaded).done(function () {

    Hyphenator.run();

    if (projectId == undefined || projectId == "") {
        alert('Project is not defined! Please, search for project to complete request! ')
    } else {
        renderTable();
    }

    $('#issues-table').find('tbody').on('click', 'tr td.details-control', function () {
        var tr = $(this).closest('tr');
        var row = dt.row(tr);
        var idx = $.inArray(tr.attr('id'), detailRows);

        if (row.child.isShown()) {
            tr.removeClass('details');
            row.child.hide();

            // Remove from the 'open' array
            detailRows.splice(idx, 1);
        }
        else {
            tr.addClass('details');
            row.child(format(row.data())).show();

            // Add to the 'open' array
            if (idx === -1) {
                detailRows.push(tr.attr('id'));
            }
        }
    });

    $('#issues-table').find('tbody').on('click', '.text-center', function () {
        if ($(this).closest('tr').parent().children()[0].className != 'dataTables_empty') {
            if ($(this).closest('tr').hasClass('selected')) {
                $(this).closest('tr').removeClass('selected');
                if(preLoaded) {
                    $('#btn-is-delete').addClass('non-visible');
                    $('#btn-is-edit').text('Create');
                }
                isChecked = false;
            }
            else {
                dt.$('tr.selected').removeClass('selected');
                $(this).closest('tr').addClass('selected');
                isChecked = true;
                if(preLoaded) {
                    $('#btn-is-edit').text('Edit');
                    $('#btn-is-delete').removeClass('non-visible');
                }
                checkedId = dt.data()[dt.rows('.selected')[0]].id;
            }
        }
    });

    $('#btn-delete-yes').on('click', function() {
        $.ajax({
            type: "DELETE",
            url: "/issue/" + checkedId,
            success: function() {
                location.reload();
            }
        });
    });

});
