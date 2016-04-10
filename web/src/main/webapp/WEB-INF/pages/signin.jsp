<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:template>

    <jsp:attribute name="header">
      <link href="<c:url value="/resources/custom_css/signin-signup.css"/>" rel="stylesheet"/>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <script src="<c:url value="/resources/custom_js/signin.js"/>"></script>
    </jsp:attribute>

    <jsp:body>
        <body style="background: url('/resources/images/macbook-5.jpg') center center no-repeat fixed; opacity: 0.9; background-size: cover;">
        <div class="col-md-4 col-md-offset-8">
            <div class="alert alert-danger non-visible alert-danger-position" id="invalid_login"
                 role="alert">
            </div>
        </div>
        <div class="login-panel">
            <div class="col-md-8" style="color: white">
                <h1>iTracker</h1>
                <h3>Tool for tracking your projects and tasks</h3>
            </div>
            <div class="col-md-4">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h2 class="panel-title">Please, Sign in or <a type="button" class="btn btn-success"
                                                                      href="<c:url value="/signup"/>">Sign up</a></h2>
                    </div>
                    <div class="panel-body">
                        <form role="form">
                            <fieldset>
                                <div class="form-group" id="form_group_email">
                                    <label class="control-label" for="email">E-mail:</label>
                                    <input class="form-control" placeholder="E-mail" id="email" type="text"
                                           autofocus>
                                </div>
                                <div class="form-group" id="form_group_password">
                                    <label class="control-label" for="password">Password:</label>
                                    <input class="form-control" placeholder="Password" id="password"
                                           type="password">
                                </div>
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" value="Remember Me" id="remember">Remember Me
                                    </label>
                                </div>
                                <button type="submit" class="btn btn-lg btn-primary btn-block"
                                        id="sign_in_btn">Sign
                                    in
                                </button>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        </body>
    </jsp:body>

</t:template>