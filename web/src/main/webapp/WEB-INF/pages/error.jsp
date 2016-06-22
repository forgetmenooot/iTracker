<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="header">
        <link href="<c:url value="/resources/custom_css/signin-signup.css"/>" rel="stylesheet"/>
    </jsp:attribute>

    <jsp:body>
        <body class="result">
        <div class="col-md-8 col-md-offset-2" style="margin-top: 25%">

            <div class="alert alert-danger  center-text"
                 role="alert"><strong>Server error!</strong><br/>Message: ${exception} <br/> Url: ${url}
            </div>
        </div>
        </body>
        <footer class="footer">
            <p>2016, iTracker.</p>
        </footer>
    </jsp:body>
</t:template>

