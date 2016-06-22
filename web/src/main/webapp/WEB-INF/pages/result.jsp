<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="header">
        <link href="<c:url value="/resources/custom_css/signin-signup.css"/>" rel="stylesheet"/>
    </jsp:attribute>

    <jsp:body>
        <body class="result">
        <div class="col-md-6 col-md-offset-3" style="margin-top: 25%">

            <div class="alert alert-danger  center-text ${error eq null? 'non-visible':''}"
                 role="alert"><strong> ${error}</strong><a class="alert-link" href="<c:url value="/register"/>"> Register</a>!
            </div>
            <div class="alert alert-success center-text ${success eq null? 'non-visible':''}"
                 role="alert"><strong>${success}</strong><a class="alert-link" href="<c:url value="/"/>"> Login</a>!
            </div>
        </div>
        </body>
        <footer class="footer">
            <p style="color: white">2016, iTracker.</p>
        </footer>
    </jsp:body>
</t:template>


