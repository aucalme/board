<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>boardWrite</title>
</head>
<script type="text/javascript">

	$j(document).ready(function(){
		
		// 행 추가 버튼기능
		$j("#addRow").on("click", function(){
			var iTr = "";
			
			iTr += "<tbody>";
			iTr += "<tr>";
			iTr += "<td width='120' align='center'>"+"Title"+"</td>";
			iTr += "<td width='400'>";
			iTr += "<input name='boardTitle' type='text' size='50' value="+"${board.boardTitle}>";
			iTr += "</td>";
			iTr += "<td rowspan='2'>";
			iTr += "<input id='delRow' type='button' value='행삭제'></td>";
			iTr += "</tr>";
			iTr += "<tr>";
			iTr += "<td height='300' align='center'>"+"Comment";
			iTr += "</td>";
			iTr += "<td valign='top'>";
			iTr += "<textarea name='boardComment' rows='20' cols='55'>"+"${board.boardComment}"+"</textarea>";
			iTr += "</td>";
			iTr += "</tr>";
			iTr += "</tbody>";
			
			$j("#table").prepend(iTr);
		});
		
		// 행 삭제 버튼기능
		$j(document).on("click","#delRow", function(){
			var t = $j("#table tbody").length;
			if(t > 2) {
				$j(this).closest("tbody").remove();
			}else{
				return false;
			}
		});
		
		$j("#submit").on("click",function(){
			var $frm = $j('.boardWrite :input');
			var param = $frm.serialize();
			
			$j.ajax({
			    url : "/board/boardWriteAction.do",
			    dataType: "json",
			    type: "POST",
			    data : param,
			    success: function(data, textStatus, jqXHR)
			    {
					alert("작성완료");
					
					alert("메세지:"+data.success);
					
					location.href = "/board/boardList.do?pageNo=1";
			    },
			    error: function (jqXHR, textStatus, errorThrown)
			    {
			    	alert("실패");
			    }
			});
		});
	});
	

</script>
<body>
<form class="boardWrite">
	<table align="center">
		<tr>
			<td align="right">
			<input id="addRow" type="button" value="행추가">
			<input id="submit" type="button" value="작성">
			</td>
		</tr>
		<tr>
			<td>
				<table border ="1" id="table"> 
					<tbody>
						<tr>
							<td width="120" align="center">
							Title
							</td>
							<td width="400">
							<input name="boardTitle" type="text" size="50" value="${board.boardTitle}"> 
							</td>
							<td rowspan="2">
							<input id="delRow" type="button" value="행삭제">
							</td>
						</tr>
						<tr>
							<td height="300" align="center">
							Comment
							</td>
							<td valign="top">
							<textarea name="boardComment"  rows="20" cols="55">${board.boardComment}</textarea>
							</td>
						</tr>	
					</tbody>
						<tr>
							<td align="center">
							Writer
							</td>
							<td>
							</td>
						</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="right">
			<a href="/board/boardList.do">List</a>
			</td>
		</tr>
	</table>
</form>	
</body>
</html>