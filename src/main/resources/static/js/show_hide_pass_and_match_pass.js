$(document).on('click', '.new-pass', function () {
    $(this).toggleClass("fa-solid fa-eye");
    var input = $("#new-pass");
    input.attr('type') === 'password' ? input.attr('type', 'text') : input.attr('type', 'password')
});
$(document).on('click', '.confirm-pass', function () {
    $(this).toggleClass("fa-solid fa-eye");
    var input = $("#confirm-pass");
    input.attr('type') === 'password' ? input.attr('type', 'text') : input.attr('type', 'password')
});
  //check match
$("#confirm-pass").keyup(function(){
    var pwd = $("#new-pass").val();
    var cpwd = $("#confirm-pass").val();
    if(pwd != cpwd){
        $("#ShowErrorPass").html("**Password are not matching!!");
        $("#ShowErrorPass").css('color','red');
        $('#btn-reset').prop('disabled', true);        
        $("#btn-reset").css('color','red')                  
        
        return true;
    }else{
        $("#ShowErrorPass").html("");
        $('#btn-reset').prop('disabled', false);
        $("#btn-reset").css('color','white')                  
        return true;
    }        
});