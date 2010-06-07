<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<h2>Test Results</h2>
  <head><title>Simple GSP page</title></head>
    <g:each in="${results}" var="result" >
        <div>
            ${result.key}...
            <g:if test="${result.value}"><span style="color: green">Passed</span></g:if>
            <g:else><span style="color: red">Fail</span></g:else>
        </div>
    </g:each>

</html>