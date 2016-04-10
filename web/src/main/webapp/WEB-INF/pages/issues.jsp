<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="header">
        <link href="<c:url value="/resources/custom_css/dashboard.css"/>" rel="stylesheet"/>
        <%--<link href="<c:url value="/resources/css/dataTables.foundation.min.css"/>" rel="stylesheet"/>--%>
        <link href="<c:url value="/resources/css/dataTables.bootstrap.min.css"/>" rel="stylesheet"/>
        <%--<link href="<c:url value="/resources/css/dataTables.jqueryui.min.css"/>" rel="stylesheet"/>--%>
        <%--<link href="<c:url value="/resources/css/jquery.dataTables.min.css"/>" rel="stylesheet"/>--%>
    </jsp:attribute>
    <jsp:attribute name="footer">
        <%--<script src="<c:url value="/resources/js/dataTables.foundation.min.js"/>"></script>--%>
        <%--<script src="<c:url value="/resources/js/dataTables.jqueryui.min.js"/>"></script>--%>
        <script src="<c:url value="/resources/js/jquery.dataTables.min.js"/>"></script>
        <script src="<c:url value="/resources/js/dataTables.bootstrap.min.js"/>"></script>
        <script src="<c:url value="/resources/js/Hyphenator.js"/>"></script>
        <script src="<c:url value="/resources/js/patterns/en-us.js"/>" type="text/javascript"></script>
        <script src="<c:url value="/resources/custom_js/issues.js"/>"></script>
        <script src="<c:url value="/resources/custom_js/create-issue.js"/>"></script>
    </jsp:attribute>

    <jsp:body>
        <t:navbar-new/>

        <div style="padding: 2%">
            <div class="panel panel-primary">

                <div class="panel-heading">
                    <i class="fa  fa-stack-overflow fa-fw"></i>Tickets
                </div>
                <div class="panel-body">
                    <table id="issues-table" class="table table-striped table-hover"
                           data-order="[[ 2, &quot;asc&quot; ]]" cellspacing="0"
                           width="100%"></table>
                </div>
            </div>
            <t:create-issue/>
        </div>

        <div class="modal fade" id="issue-modal-delete" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                                aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">Delete</h4>
                    </div>
                    <div class="modal-body">
                        <p>Are you sure you want to delete selected issue?</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" id="btn-delete-yes">Delete</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:template>

