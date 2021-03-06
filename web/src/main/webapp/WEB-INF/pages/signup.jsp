<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="header">
        <link href="<c:url value="/resources/custom_css/signin-signup.css"/>" rel="stylesheet"/>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <script src="<c:url value="/resources/custom_js/signup.js"/>"></script>
    </jsp:attribute>

    <jsp:body>
        <body class="register">
        <div class="col-md-4">
            <div class="alert non-visible alert-danger-position" id="alert-message"
                 role="alert"></div>
        </div>
        <div class="login-panel">
            <div class="col-md-4">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h2 class="panel-title">Register</h2>
                    </div>
                    <div class="panel-body">
                        <form role="form">
                            <fieldset>
                                <div class="form-group" id="form_group_full_name">
                                    <label class="control-label" for="full_name">Full name:</label>
                                    <input class="form-control" placeholder="Full name" id="full_name" type="text"
                                           autofocus>
                                </div>
                                <div class="form-group" id="form_group_email">
                                    <label class="control-label" for="email">E-mail:</label>
                                    <input class="form-control" placeholder="E-mail" id="email" type="text">
                                </div>
                                <div class="form-group" id="form_group_password">
                                    <label class="control-label" for="password">Password:</label>
                                    <input class="form-control" placeholder="Password" id="password" type="password">
                                </div>
                                <button type="submit" class="btn btn-lg btn-primary btn-block" id="sign_up_btn">Register
                                </button>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
            <div class="col-md-8" style="color: white">
                <h3>Join iTracker and simplify development as never before</h3>
            </div>
        </div>
        </body>
        <footer class="footer">
            <p style="color: white">2016, iTracker.</p>
        </footer>
    </jsp:body>

</t:template>