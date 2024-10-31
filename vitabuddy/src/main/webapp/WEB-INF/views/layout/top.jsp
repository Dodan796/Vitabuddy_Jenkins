<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!-- top 메뉴 -->    
<header>
    <div id="headerBox">
        <div id="logoBox">
            <%-- <a href="<c:url value='/home'/>"><img src="<c:url value=''/>" id="logoImg"></a> --%>
            <a href="<c:url value='/home'/>">VITABUDDY</a>
        </div>
        <div id="topMenu"> 
        
        <!-- 로그인 하지 않은 경우 -->
        <c:if test="${empty sessionScope.sid}">
            <!-- 여기를 수정하여 로그인 링크를 /intro로 설정 -->
            <a href="<c:url value='/intro'/>">로그인</a>                   
            <a href="<c:url value='/member/register'/>">회원가입</a>
        </c:if>
        
        <!-- 로그인 한 경우 -->
        <c:if test="${not empty sessionScope.sid}">
            <a href="<c:url value='/member/logout'/>">로그아웃</a>
            <a href="<c:url value='/supplement/wishList'/>"><i class="fa-solid fa-heart"></i></a>                    
            <a href="<c:url value='/supplement/cartList'/>"><i class="fa-solid fa-cart-shopping"></i></a>
            <a href="<c:url value='/member/myPage'/>"><i class="fa-solid fa-user"></i></a>
        </c:if>
        </div>
    </div>
    <hr>
</header>