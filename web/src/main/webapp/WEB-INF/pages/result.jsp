<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>
        <body style="background: url('/resources/images/macbook-7.jpg') center center no-repeat fixed; opacity: 0.9; background-size: cover;">
        <div class="col-md-6 col-md-offset-3" style="margin-top: 25%">

            <div class="alert alert-danger  center-text ${error eq null? 'non-visible':''}"
                 role="alert"><strong> ${error}</strong><a class="alert-link" href="<c:url value="/signup"/>"> Sign up</a>!
            </div>
            <div class="alert alert-success center-text ${success eq null? 'non-visible':''}"
                 role="alert"><strong>${success}</strong><a class="alert-link" href="<c:url value="/"/>"> Sign in</a>!
            </div>
        </div>
        </body>
    </jsp:body>
</t:template>


