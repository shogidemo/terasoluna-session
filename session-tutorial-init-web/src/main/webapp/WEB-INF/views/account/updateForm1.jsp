<div>
    <%-- 入力データを受け取るフォームオブジェクトの属性名を modelAttribute 属性に指定する。
         上記例は、属性名が accountUpdateForm のオブジェクトが入力データを受け取る。 --%>
    <form:form action="${pageContext.request.contextPath}/account/update"
               method="post" modelAttribute="accountUpdateForm">

        <h2>Account Update Page 1/2</h2>
        <table>
            <tr>
                <td><form:label path="name" cssErrorClass="error-label">name</form:label></td>
                    <%-- form:input タグの path 属性に入力データを格納するオブジェクトの要素名を指定する。
                         この方法を利用すると、指定したオブジェクトの要素名にすでにデータがある場合、その値が入力フォームのデフォルト値となる。 --%>
                <td><form:input path="name" cssErrorClass="error-input" />
                    <form:errors path="name" cssClass="error-messages" />
                </td>
            </tr>
            <tr>
                <td><form:label path="email" cssErrorClass="error-label">e-mail</form:label></td>
                <td><form:input path="email" cssErrorClass="error-input" />
                    <form:errors path="email" cssClass="error-messages" />
                </td>
            </tr>
            <tr>
                <td><form:label path="birthday" cssErrorClass="error-label">birthday</form:label></td>
                <td><fmt:formatDate value="${accountUpdateForm.birthday}" pattern="yyyy-MM-dd" var="formattedBirthday" />
                    <input type="date" id="birthday" name="birthday" value="${formattedBirthday}">
                    <form:errors path="birthday" cssClass="error-messages" />
                </td>
            </tr>
            <tr>
                <td><form:label path="zip" cssErrorClass="error-label">zip</form:label></td>
                <td><form:input path="zip" cssErrorClass="error-input" />
                    <form:errors path="zip" cssClass="error-messages" />
                </td>
            </tr>
            <tr>
                <td><form:label path="address" cssErrorClass="error-label">address</form:label></td>
                <td><form:input path="address" cssErrorClass="error-input" />
                    <form:errors path="address" cssClass="error-messages" />
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><input type="submit" name="form2" id="next" value="next" /></td>
            </tr>
        </table>
    </form:form>

    <form method="get" action="${pageContext.request.contextPath}/account/update">
        <input type="submit" name="home" id="home" value="home" />
    </form>
</div>