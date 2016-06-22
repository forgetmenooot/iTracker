<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>

    <jsp:attribute name="footer">
         <script src="<c:url value="/resources/custom_js/main.js"/>"></script>
         <script src="<c:url value="/resources/custom_js/create-issue.js"/>"></script>
    </jsp:attribute>

    <jsp:body>

        <t:navbar/>

        <div style="padding: 2%">

            <ul class="nav nav-pills" id="project-names">
                <a data-toggle="modal" data-target="#archive-project-modal"
                   class="btn btn-danger pull-right" id="btn-archive-project">Archive project</a>
                <a data-toggle="modal" data-target="#project-modal" style="margin-left: 1%;margin-right: 1%"
                   class="btn btn-warning pull-right" id="btn-edit-project">Edit project</a>
                <a data-toggle="modal" data-target="#issue-modal"
                   class="btn btn-success pull-right" id="btn-create-issue">Create ticket</a>
            </ul>

            <div class="tab-content" id="pr-content"></div>

        </div>

        <div class="modal fade" id="archive-project-modal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" id="btn-close-arch-project"
                                aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">Archive</h4>
                    </div>
                    <div class="modal-body">
                        <div class="alert alert-danger non-visible center-text" id="invalid-project-archive"
                             role="alert"></div>
                        Are you sure you want to archive project?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" id="btn-arch-project">Archive</button>
                        <button type="button" class="btn btn-default" id="btn-cancel-arch-project" data-dismiss="modal">
                            Cancel
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <t:create_issue/>
        <footer class="footer">
            <p>2016, iTracker.</p>
        </footer>
    </jsp:body>


</t:template>



