<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">

    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand brand-title" href="<c:url value="/dashboard"/>">iTracker</a>
    </div>

    <form class="navbar-form navbar-left" role="search">
        <div class="form-group" id="form-group-search">
            <input type="text" class="form-control" id="search-pr" placeholder="Search project">
        </div>
        <button type="button" class="btn btn-outline btn-primary" id="btn-search-project">Search</button>
        <span class="alert non-visible alert-danger" id="search-error" style="padding: 8px"
             role="alert"></span>
    </form>

    <ul class="nav navbar-top-links navbar-right" style="margin-top: 9px">
        <li><button type="button" data-toggle="modal" data-target="#project-modal"
                    class="btn btn-outline btn-primary" id="btn-create-project">Create project</button></li>
        <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#" aria-expanded="false">
                ${fullName}<i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
            </a>
            <ul class="dropdown-menu dropdown-user">
                <li><a href="<c:url value="/api/auth/logout"/>"><i class="fa fa-sign-out fa-fw"></i> Logout</a></li>
            </ul>
        </li>
    </ul>
</nav>

<input type="hidden" value="${userId}" id="user-session-id"/>

<div class="modal fade" id="project-modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <input type="hidden" id="pr-id"/>
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" id="btn-close-project" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="modal-name-project"></h4>
            </div>
            <div class="modal-body">
                <fieldset>
                    <div class="form-group col-md-12">
                        <div class="alert alert-danger non-visible center-text" id="invalid-project-add"
                             role="alert"></div>
                    </div>
                    <div class="form-group col-md-12" id="form-group-pr-name">
                        <label class="control-label" for="pr-name">Name:</label>
                        <input class="form-control" placeholder="Name" id="pr-name" type="text">
                    </div>
                    <div class="form-group col-md-12" id="form-group-users">
                        <label class="control-label" for="pr-user">Users:</label>
                        <input class="form-control" placeholder="Name" id="pr-user" type="text">

                        <div id="pr-users-list" class="padding-top"></div>
                    </div>
                    <div class="form-group col-md-12" id="form-pr-group-desc">
                        <label class="control-label" for="pr-desc">Description:</label>
                        <textarea class="form-control" placeholder="Description" rows="4" id="pr-desc"></textarea>
                    </div>

                </fieldset>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" id="btn-save-project">Save</button>
                <button type="button" class="btn btn-default" id="btn-cancel-project" data-dismiss="modal">Cancel
                </button>
            </div>
        </div>
    </div>
</div>