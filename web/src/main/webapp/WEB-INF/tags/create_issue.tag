<div class="modal fade" id="issue-modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" id="btn-close-issue" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="modal-name-issue"></h4>
            </div>
            <div class="modal-body">
                <fieldset>
                    <div class="form-group col-md-12">
                        <div class="alert alert-danger non-visible text-center" id="invalid-issue-edit"
                             role="alert"></div>
                    </div>
                    <div class="form-group col-md-12" id="form-group-is-name">
                        <label class="control-label" for="is-name">Name:</label>
                        <input class="form-control" placeholder="Name" id="is-name" type="text">
                    </div>
                    <div class="form-group col-md-6 non-visible" id="form-group-status">
                        <label class="control-label" for="is-status">Status:</label>
                        <select id="is-status" class="form-control">
                            <option>OPENED</option>
                            <option>INPROGRESS</option>
                            <option>RESOLVED</option>
                            <option>CLOSED</option>
                        </select>
                    </div>
                    <div class="form-group col-md-6" id="form-group-category">
                        <label class="control-label" for="is-category">Category:</label>
                        <select id="is-category" class="form-control">
                            <option>ISSUE</option>
                            <option>BUG</option>
                            <option>IMPROVEMENT</option>
                        </select>
                    </div>
                    <div class="form-group col-md-6" id="form-group-priority">
                        <label class="control-label" for="is-priority">Priority:</label>
                        <select id="is-priority" class="form-control">
                            <option>MINOR</option>
                            <option>TRIVIAL</option>
                            <option>MAJOR</option>
                            <option>BLOCKER</option>
                            <option>CRITICAL</option>
                        </select>
                    </div>
                    <div class="form-group col-md-8" id="form-group-assignee">
                        <label class="control-label" for="is-assignee">Assignee:</label>
                        <input class="form-control" placeholder="Assignee" type="text" id="is-assignee">

                        <div id="assignee-field" class="padding-top"></div>
                    </div>
                    <div class="form-group col-md-4" id="form-group-version">
                        <label class="control-label" for="is-version">Version:</label>
                        <input class="form-control" placeholder="Version" type="number" id="is-version">
                    </div>
                    <div class="form-group col-md-12" id="form-group-is-desc">
                        <label class="control-label" for="is-desc">Description:</label>
                        <textarea class="form-control" placeholder="Description" rows="4" id="is-desc"></textarea>
                    </div>
                </fieldset>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" id="btn-save-issue">Save</button>
                <button type="button" class="btn btn-default" id="btn-cancel-issue" data-dismiss="modal">Cancel
                </button>
            </div>
        </div>
    </div>
</div>