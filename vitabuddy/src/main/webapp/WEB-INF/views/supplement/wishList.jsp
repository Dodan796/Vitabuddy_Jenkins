<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>찜 목록</title>
    <c:import url="/WEB-INF/views/layout/head.jsp" />
    <script src="<c:url value='/js/deleteWish.js'/>"></script>
    <script src="<c:url value='/js/addCartWish.js'/>"></script>	
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/wishList.css'/>">
</head>
<body>
    <c:import url="/WEB-INF/views/layout/top.jsp" /> 
    <section id="wrap">        		
        <div class="container">
            <h1>찜 목록</h1>
            <div class="supplementList">
                <table class="wishList">
                    <colgroup> 
                        <col width="50">
                        <col width="170">
                        <col width="*">
                        <col width="143">
                    </colgroup>	
                    <tbody class="listBody">
					    <c:choose>
					        <c:when test="${not empty wishList}">
					            <c:forEach var="wishList" items="${wishList}" varStatus="status">
					                <tr class="wishItem">
					                    <td>${status.index + 1}</td> <!-- 인덱스 번호를 출력 -->
					                    <td class="itemImage">
					                        <a href="<c:url value='/api/supplement/supplementDetail/${wishList.supId}'/>">
					                            <img class="supImg" src="data:image/png;base64,${wishList.base64SupImg}" width="150" height="150">
					                        </a>
					                    </td>
					                    <td class="supDetail">
					                        <p>상품명: ${wishList.supName}</p>
					                        <p>브랜드: ${wishList.supBrand}</p>
					                        <p>가격: ₩<fmt:formatNumber value="${wishList.supPrice}" pattern="#,###" /></p>
					                    </td>
					                    <td class="status">
					                        <div class="plusCartBtn" type="button" data-sup-id="${wishList.supId}" data-user-id="${sessionScope.sid}">
					                            <i class="fa-solid fa-cart-plus" value="장바구니에 담기"></i>
					                        </div>
					                        <p></p>
					                        <div class="deleteCartBtn" type="button" data-sup-id="${wishList.supId}" data-user-id="${sessionScope.sid}">
					                            <i class="fa-solid fa-trash" value="찜 목록에서 삭제하기"></i>
					                        </div>
					                    </td>
					                </tr>
					            </c:forEach>
					        </c:when>                    
					        <c:otherwise>
					            <tr>
					                <td colspan="4" align="center">찜 목록이 없습니다.</td>
					            </tr>
					        </c:otherwise>  
					    </c:choose>
					</tbody>
                </table>
            </div>
        </div>
    </section>
    <c:import url="/WEB-INF/views/layout/footer.jsp" /> 
</body>
</html>