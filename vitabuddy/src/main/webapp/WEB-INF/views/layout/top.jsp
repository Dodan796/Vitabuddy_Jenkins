<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<header>
	<div id="headerBox">
		<div id="logoBox">
            <a href="<c:url value='/home'/>"><img src="<c:url value='/image/logo.png'/>" id="logoImg"  width="240" height="80"></a>
            <%-- <a href="<c:url value='/home'/>">VITABUDDY</a> --%>
        </div>
		<div id="topMenu">
			<!-- 글자크기 조절 버튼 => 값은 논의후 결정.-->
			<div class="font-size-selector">
				<label for="fontSize">글자크기</label> <select id="fontSize"
					onchange="adjustFontSize(this.value)">
					<option value="1">일반</option>
					<option value="2">중간</option>
					<option value="3">크게</option>
					<option value="4">매우크게</option>
				</select>
			</div>
			<!-- 로그인 및 회원가입 -->
			<c:if test="${empty sessionScope.sid}">
				<a href="<c:url value='/intro'/>">로그인</a>
				<a href="<c:url value='/member/register'/>">회원가입</a>
			</c:if>
			<!-- 로그아웃 및 유저 메뉴 -->
			<c:if test="${not empty sessionScope.sid}">
				<a href="<c:url value='/member/logout'/>">로그아웃</a>
				<a href="<c:url value='/supplement/wishList'/>"><i
					class="fa-solid fa-heart"></i></a>
				<a href="<c:url value='/supplement/cartList'/>"><i
					class="fa-solid fa-cart-shopping"></i></a>
				<a href="<c:url value='/member/myPage'/>"><i
					class="fa-solid fa-user"></i></a>
			</c:if>
		</div>
	</div>
	<hr>
</header>