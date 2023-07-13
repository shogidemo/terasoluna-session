<sec:authentication property="principal" var="userDetails" />

<div style="display: inline-flex">
    welcome&nbsp;&nbsp; <span id="userName">${f:h(userDetails.account.name)}</span>
    <form:form method="post" action="${pageContext.request.contextPath}/logout">
        <input type="submit" id="logout" value="logout" />
    </form:form>
    <form method="get" action="${pageContext.request.contextPath}/account/update">
        <input type="submit" name="form1" id="updateAccount" value="Account Update" />
    </form>
</div>
<br>
<br>

<div>
    <spring:eval var="cart" expression="@cart" />
    <form:form method="post"
               action="${pageContext.request.contextPath}/cart"
               modelAttribute="cartForm">
        <form:errors path="removedItemsIds" cssClass="error-messages" />
        <t:messagesPanel />
        <table>
            <tr>
                <th>Name</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Remove</th>
            </tr>
            <c:forEach items="${cart.cartItems}" var="cartItem" varStatus="status">
                <tr>
                    <td id="itemName${status.index}">${f:h(cartItem.goods.name)}</td>
                    <td id="itemPrice${status.index}"><fmt:formatNumber value="${cartItem.goods.price}" type="CURRENCY" currencySymbol="&yen;" maxFractionDigits="0" /></td>
                    <td id="itemQuantity${status.index}">${f:h(cartItem.quantity)}</td>
                        <%-- チェックボックスを利用して、削除する商品を指定する。
                             チェックボックスが選択された状態で削除ボタンが押されると、該当商品のIDがサーバに送信される。
                         --%>
                    <td><input type="checkbox" name="removedItemsIds" id="removedItemsIds${status.index}" value="${f:h(cartItem.goods.id)}" /></td>
                </tr>
            </c:forEach>
            <tr>
                <td>Total</td>
                <td id="totalPrice"><fmt:formatNumber value="${f:h(cart.totalAmount)}" type="CURRENCY" currencySymbol="&yen;" maxFractionDigits="0" /></td>
                <td></td>
                <td></td>
            </tr>
        </table>
        <input type="submit" id="remove" value="remove" />
    </form:form>
</div>

<div style="display: inline-flex">
    <form method="get" action="${pageContext.request.contextPath}/order">
        <input type="submit" id="confirm" name="confirm" value="confirm your order" />
    </form>
    <form method="get" action="${pageContext.request.contextPath}/goods">
        <input type="submit" id="home" value="home" />
    </form>
</div>