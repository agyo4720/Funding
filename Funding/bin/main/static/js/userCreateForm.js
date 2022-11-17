// 우편번호 API
function btnPostCode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = '';
            var extraAddr = '';

            if(data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            if(data.userSelectedType === 'R') {
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }

                if(extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }

                document.getElementById("extraAddress").value = extraAddr;
            } else {
                document.getElementById("extraAddress").value = '';
            }

            document.getElementById("postCode").value = data.zonecode;
            document.getElementById("address").value = addr;
            document.getElementById("detailAddress").focus();
        }
    }).open();
}