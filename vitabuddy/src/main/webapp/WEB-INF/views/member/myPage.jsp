<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/myPage.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/head.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/footer.css'/>">
<c:import url="/WEB-INF/views/layout/head.jsp"/>
<meta charset="UTF-8">
<title>마이페이지</title>
</head>
<body>
<c:import url="/WEB-INF/views/layout/top.jsp" />
    <section id="wrap">
        <div class="container">
            <div class="headers">
                    <h1>마이페이지</h1>
            </div>
			<div class="box"><!--10/21 클래스명 horizental-box>box로 수정-->
               <!--  <form> --><!-- 10/21 폼 태그 삭제 -->
                    <!-- 복용 중인 영양제 -->
                    <p>복용 중인 영양제</p>
				<div class="mySupplement">
				    <c:forEach var="supplement" items="${userSupplements}" varStatus="status">
				        <div class="supplement-item">
				            <span>${fn:trim(supplement.supName)} - ${fn:trim(supplement.supBrand)}</span>
				  		</div>
				    </c:forEach>
				</div>
                    
                    <!-- <label>올바른 영양제 복용법</label>
                    <textarea textarea rows="10%" cols="100%"readonly></textarea>    -->
                    
                    <div class="box_rowContents" >
<!-- div의 readonly 삭제 --><div class="horizontal_box">
            <!-- label>p --><p>추천 성분</p>
<!--10/21 textarea>div로 변경--><div class="recommend">
					<!-- 내용 추가할 떄 반복문 안 요소들을 아래 <div> 태그로 묶어주세요 -->
					<div class="recommentList">
						    <!--<c:forEach var="entry" items="${allRecommendLists}">
							    <b>${entry.key + 1}.</b>
							    <c:forEach var="recommendVO" items="${entry.value}">
							        <c:choose>
							            
							            <c:when test="${not empty recommendVO}">
							                <b>추천 성분</b>: ${recommendVO.ingredients} <br>
							                <b>영양제 궁합</b>: ${recommendVO.interactionRecommend} <br>
							                <br>
							            </c:when>
							            <c:otherwise>
							                <b>추천 성분</b>: [] <br>
							            </c:otherwise>
							        </c:choose>
							    </c:forEach>
							</c:forEach>-->
							<c:forEach var="entry" items="${allRecommendLists}" >
							    <b>${entry.key + 1}.</b> <!-- key 대신 index로 순서 지정 -->
							    <c:choose>
							        <c:when test="${not empty entry.value}">
							            <c:forEach var="recommendVO" items="${entry.value}">
							                <b>추천 성분</b>: ${recommendVO.ingredients} <br>
							                <b>영양제 궁합</b>: ${recommendVO.interactionRecommend} <br>
							                <br>
							            </c:forEach>
							        </c:when>
							        <c:otherwise>
							            <!-- 데이터가 없을 때 []를 출력 -->
							            <b>추천 성분</b>: [ ] <br><br>
							        </c:otherwise>
							    </c:choose>
							</c:forEach>
					</div>

							</div>	
                        </div>
<!-- div의 readonly 삭제 --><div class="horizontal_box">
            <!-- label>p --><p>성분간 상호작용</p>
						<!--10/21 textarea>div로 변경-->
						<div class="interaction">
							<c:choose>
								<c:when test="${not empty interactions}">
									<c:forEach var="interaction" items="${interactions}"
										varStatus="status">
										<c:if test="${interaction.ingredient2 != '해당 없음' && interaction.interactionDetail != '상호작용 정보 없음'}">
											<!-- 10/21 div 태그 추가 -->
											<div class="interactionList">												
												<li>
												<strong>[${interaction.ingredient1} - ${interaction.ingredient2}]&#13;&#10;</strong><br>
												<b>문제</b>: ${fn:trim(interaction.interactionDetail)}&#13;&#10;<br><br> 												
												<b>올바른 복용법</b>: ${fn:trim(interaction.interactionDosage)}&#13;&#10;&#13;&#10;
												</li>
											</div>
										</c:if>
									</c:forEach>
								</c:when>
							</c:choose>
						</div>
					</div>
				</div>
                <!-- </form> --><!-- 10/21 폼 태그 삭제 -->
            </div>
            <!-- 예시: sessionScope에서 사용자 ID 가져와서 URL 파라미터로 추가 -->
            <button class="submit-btn" onclick="location.href='/member/myInfoChangeForm?userId=${sessionScope.sid}'">회원 정보/ 영양제 관리</button>
            <!-- 내가 작성한 리뷰 목록 -->
                    <div class="review-section">
                        <h3>내가 작성한 리뷰 목록</h3>
                        <form action="/myPage/reviews" method="get">
                            <label for="sort">정렬 기준:</label>
                            <select name="sort" id="sort" onchange="this.form.submit()">
                                <option value="recent">최신순</option>
                                <option value="oldest">오래된 순</option>
                            </select>
                        </form>
                        <table id="reviewList">
                            <thead>
                                <tr>
<!-- 10/21 th 클래스명 추가 -->
                                    <th class="myReviewNo">번호</th>
                                    <th class="important">제목</th>
                                    <th class="supName">상품명</th>
                                    <th class="reviewDate">작성일</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty reviews}">
                                    <c:forEach var="review" items="${reviews}" varStatus="status">
                                        <tr>
                                            <td>${status.index + 1}</td> <!-- 리뷰 번호를 1부터 시작 -->
                                            <td class="important">${review.reviewTitle}</td>
                                            <td>${review.supName}</td> <!-- 상품명을 표시할 컬럼 -->
                                            <td><fmt:formatDate value="${review.reviewDate}" pattern="yyyy-MM-dd" /></td>
                                        </tr>
                                    </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="4" align="center">작성하신 리뷰가 없습니다.</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                    <!-- 구매 내역 -->
                    <div class="purchase-section">
                        <h3>구매 내역</h3>
                        <select>
                            <option>현재 주문 처리 상태</option>
                            <option>입금 전</option>
                            <option>배송 준비 중</option>
                            <option>배송 중</option>
                            <option>배송 완료</option>
                            <option>취소</option>
                            <option>반품</option>
                            <option>환불</option>
                        </select>
<!-- 10/21 br태그 추가 --><br>
                        <table width="100%" id="productInfo">
                            <thead>
                                <tr>
                                    <th>주문일자</th>
                                    <th>이미지</th>
                                    <th>상품정보</th>
                                    <th>수량</th>
                                    <th>금액</th>
                                    <th>주문 처리</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td colspan="6" align="center">주문 내역이 없습니다.</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </section>
    <c:import url="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>