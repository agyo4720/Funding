// 유효성 검사 부분
var btn = document.querySelector('#btn');
var valid = false
let fundTime

let error = document.querySelectorAll(".errorList")

let subject = document.querySelector('.subject');
let aertiest = document.querySelector('.aertiest');
let place = document.querySelector('.place');
let runtime = document.querySelector('.runtime');
let fundDurationE = document.querySelector('.fundDurationE');
let startDate = document.querySelector('.startDate');
let minFund = document.querySelector('.minFund');
let fundAmount = document.querySelector('.fundAmount');
let content = document.querySelector('.content');

btn.addEventListener('click', function(e) {
    let count = 0;
    for(i=0; i < error.length ; i++){
        if(error[i].style.display == 'none'){
            count += 1
        }
    }
    
    if(count == error.length){
        valid = true
    }
    
    if(valid){
        
    }else{
        e.preventDefault();
    }
});

subject.addEventListener('focusout',checkSub)
aertiest.addEventListener('focusout',checkAertiest)
place.addEventListener('focusout',checkPlace)
runtime.addEventListener('focusout',checkRuntime)
fundDurationE.addEventListener('focusout',checkFundDurationE)
startDate.addEventListener('focusout',checkStartDate)
minFund.addEventListener('focusout',checkMinFund)
fundAmount.addEventListener('focusout',checkFundAmount)
content.addEventListener('focusout',checkCon)


function checkSub(){
    if(subject.value === ""){
        error[0].innerHTML='빈칸이에용.'
        error[0].style.display='block'
    }else{
        error[0].style.display='none'
    }
}
function checkAertiest(){
    if(aertiest.value === ""){
        error[1].innerHTML='빈칸이에용.'
        error[1].style.display='block'
    }else{
        error[1].style.display='none'
    }
}
function checkPlace(){
    if(place.value === ""){
        error[2].innerHTML='빈칸이에용.'
        error[2].style.display='block'
    }else{
        error[2].style.display='none'
    }
}
function checkRuntime(){
    if(runtime.value === ""){
        error[3].innerHTML='숫자입력해줘용.'
        error[3].style.display='block'
    }else{
        error[3].style.display='none'
    }
}
function checkFundDurationE(){
    if(fundDurationE.value === ""){
        error[4].innerHTML='빈칸이에용.'
        error[4].style.display='block'
    }else{
        error[4].style.display='none'
        fundTime = document.querySelector('.fundDurationE').value
        fundTime += 'T00:00'
        startDate.min = fundTime
    }
}
function checkStartDate(){
    if(startDate.value === ""){
        error[5].innerHTML='빈칸이에용.'
        error[5].style.display='block'
    }else{
        error[5].style.display='none'
    }
}
function checkMinFund(){
    if(minFund.value === ""){
        error[6].innerHTML='빈칸이에용.'
        error[6].style.display='block'
    }else if(parseInt(minFund.value) < parseInt(1000)){
        error[6].innerHTML='1000원 이상해주세요.'
        error[6].style.display='block'
    }else{
        error[6].style.display='none'
    }
}
function checkFundAmount(){
    if(fundAmount.value === ""){
        error[7].innerHTML='빈칸이에용.'
        error[7].style.display='block'
    }else{
        error[7].style.display='none'
    }
}
function checkCon(){
    if(content.value === ""){
        error[8].innerHTML='빈칸이에용.'
        error[8].style.display='block'
    }else{
        error[8].style.display='none'
    }
}
// --유효성 검사 끝


// 썸머노트 부분
$(document).ready(function() {
    $('#summernote').summernote({
        height: 300,
        width: 740,
        lang: "ko-KR",
        toolbar: [
            // [groupName, [list of button]]
            ['fontname', ['fontname']],
            ['fontsize', ['fontsize']],
            ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
            ['color', ['forecolor','color']],
            ['table', ['table']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['insert',['picture','link','video']],
            ['view', ['fullscreen', 'help']]
        ],
        fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
        fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72']
        ,callbacks: {	
            onImageUpload : function(files) {
                uploadSummernoteImageFile(files[0],this);
            }
        }
    })
})

// 썸머노트 이미지 업로드
function uploadSummernoteImageFile(file, editor) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
        
    data = new FormData();
    data.append("file", file);
    $.ajax({
        data : data,
        type : "POST",
        url : "/file/summernote/file",
        contentType : false,
        processData : false,
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            //항상 업로드된 파일의 url이 있어야 한다.
            $(editor).summernote('editor.insertImage', data);
        }
    });
}