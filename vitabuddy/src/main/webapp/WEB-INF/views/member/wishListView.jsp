<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>장바구니</title>
 		<c:import url="/WEB-INF/views/layout/head.jsp" />	
 		 	<link rel="stylesheet" type="text/css" href="<c:url value='/css/wishList.css'/>">	   	      
	</head>
	<body>
		<div id="wrap">
        	<!--  top -->         
         	<c:import url="/WEB-INF/views/layout/top.jsp" /> 
		<section>		
		<div class="cart-container">
		    <h1>찜목록</h1>
		    
		    <div class="productList">
				    <div class="productItem">
				        <a href="<c:url value='/product/deleteWish'/>" id="deleteWish" data-prd-id="${sup.supNo}"><i class="fa-solid fa-trash"></i></a>
				        <a href="<c:url value='/product/detailView/${sup.supNo}'/>">
				            <img src="${sup.supImg}">
				            <h3>${sup.supName}</h3>
				        </a>
				        <p>${sup.supPrice}</p>
				        <p>${sup.supCompany}</p>
				        <a href="<c:url value='/product/addCart'/>" id="addCart" data-prd-id="${sup.supNo}">장바구니 추가</a>
				    </div>
				
				    <div class="productItem">
				        <a href="<c:url value='/product/deleteWish'/>" id="deleteWish" data-prd-id="${prd.prdNo}"><i class="fa-solid fa-trash"></i></a>
				        <a href="<c:url value='/product/detailView/${prd.prdNo}'/>">
				            <img src="${sup.supImg}">
				            <h3>${sup.supName}</h3>
				        </a>
				        <p>${sup.supPrice}</p>
				        <p>${sup.supCompany}</p>
				        <a href="<c:url value='/product/addCart'/>" id="addCart" data-prd-id="${prd.prdNo}">장바구니 추가</a>
				    </div>
				
				    <div class="productItem">
				        <a href="<c:url value='/product/deleteWish'/>" id="deleteWish" data-prd-id="${prd.prdNo}"><i class="fa-solid fa-trash"></i></a>
				        <a href="<c:url value='/product/detailView/${prd.prdNo}'/>">
				            <img src="sup.supImg">
				            <h3>sup.supName</h3>
				        </a>
				        <p>sup.supPrice</p>
				        <p>sup.supCompany</p>
				        <a href="<c:url value='/product/addCart'/>" id="addCart" data-prd-id="${prd.prdNo}">장바구니 추가</a>
				    </div>
   
			</div>

		    	<%-- <c:forEach var="wish" items="${wishList}">
				
					<div class="productItem">
				        <a href="<c:url value='/product/deleteWish'/>" id="deleteWish" data-prd-id="${prd.prdNo}"><i class="fa-solid fa-trash"></i></a>
				        <a href="<c:url value='/product/detailView/${prd.prdNo}'/>">
				            <img src="${sup.supImg}">
				            <h3>${sup.supName}</h3>
				        </a>
				        <p>${sup.supPrice}</p>
				        <p>${sup.supCompany}</p>
				        <a href="<c:url value='/product/addCart'/>" id="addCart" data-prd-id="${prd.prdNo}">장바구니 추가</a>
			        </div>
				 </c:forEach> --%>
				 
			<nav>
				<!-- 페이지네이션 -->
					<div class="pagination">
					<!-- 현재 페이지에 따라 이전/다음 버튼 비활성화 -->
					    <a href="#" class="prev <c:if test='${currentPage == 1}'>disabled</c:if>" onclick="changePage(currentPage - 1)"><i class="fa-solid fa-caret-left"></i></a>
					    <c:forEach var="i" begin="1" end="${totalPages}">
					        <a href="#" class="page" onclick="changePage(${i})">${i}</a>
					    </c:forEach>
					    <a href="#" class="next <c:if test='${currentPage == totalPages}'>disabled</c:if>" onclick="changePage(currentPage + 1)"><i class="fa-solid fa-caret-right"></i></a>
					</div>
			</nav>	
				
				
		</div>
			</section> 
			
			<!--  footer -->         
 	        	<c:import url="/WEB-INF/views/layout/footer.jsp" /> 
	      </div>
	</body>
</html>