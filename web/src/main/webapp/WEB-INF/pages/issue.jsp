<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="header">
        <link href="<c:url value="/resources/css/bootstrap-editable.css"/>" rel="stylesheet"/>
        <link href="<c:url value="/resources/custom_css/general.css"/>" rel="stylesheet"/>
        <link href="<c:url value="/resources/custom_css/issue.css"/>" rel="stylesheet"/>
    </jsp:attribute>
    <jsp:attribute name="footer">
        <script src="<c:url value="/resources/js/bootstrap-editable.min.js"/>"></script>
        <script src="<c:url value="/resources/js/Hyphenator.js"/>" type="text/javascript"></script>
        <script src="<c:url value="/resources/js/patterns/en-us.js"/>" type="text/javascript"></script>
        <script src="<c:url value="/resources/js/sock.min.js"/>"></script>
        <script src="<c:url value="/resources/js/stomp.js"/>"></script>
        <script src="<c:url value="/resources/custom_js/issue.js"/>" type="text/javascript"></script>
    </jsp:attribute>

    <jsp:body>
        <t:navbar/>
        <div style="padding: 2%">

            <div class="row">
                <div class="col-md-6">
                    <div class="panel panel-primary">
                        <div class="panel-heading" id="issue-name">
                            <i class="fa fa-tag fa-fw"></i>
                        </div>
                        <div class="panel-body" style="height: 510px">
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <tbody id="issue-info"></tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="chat-panel panel panel-primary">
                        <div class="panel-heading">
                            <i class="fa fa-comments fa-fw"></i>
                            Comments
                        </div>
                        <div class="panel-body" style="height:421px">
                            <ul class="chat" id="issue-comments"></ul>
                        </div>
                        <div class="panel-footer">
                            <p style="color: red" id="invalid-input"></p>
                            <div class="input-group" id="form-group-message" style="margin-bottom: 2%;">
                            <textarea id="message" class="form-control input-sm"
                                      placeholder="Type your message here..."></textarea>
                                <span class="input-group-btn">
                                    <button class="btn btn-success btn-sm" id="btn-chat-send" style="height: 48px">
                                        Send
                                    </button>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="issue-div col-md-6" id="form-group-upload">
                    <label class="control-label">New attachments:</label>
                    <div id="fileTable" class="btn-group"></div>
                    <br/>
                    <button id="addFile" type="button" class="btn btn-primary btn-sm">Add file</button>
                    <button id="uploadFile" type="button" class="btn btn-primary btn-sm">Upload</button>
                </div>
                <div class="issue-div col-md-6" id="form-group-uploaded">
                    <label class="control-label">Attached files:</label>
                    <div id="files" class="btn-group"></div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="modalDeleteAtt" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" id="btn-close"
                                aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="modal-name">Delete</h4>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete attachment?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" id="btn-del-att">Delete</button>
                        <button type="button" class="btn btn-default" id="btn-cancel-att"
                                data-dismiss="modal">Cancel
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <footer class="footer">
            <p>2016, iTracker.</p>
        </footer>
    </jsp:body>
</t:template>


