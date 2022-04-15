// DOM読み込みが完了してから処理
document.addEventListener("DOMContentLoaded",function(){
    var select = document.getElementById('color');
    document.querySelector("#color").style.backgroundColor =
        select.options[select.options.selectedIndex].text;
    select.addEventListener("change", (e) => {
    document.querySelector("#color").style.backgroundColor =
        e.currentTarget.options[e.currentTarget.selectedIndex].text;
      e.stopPropagation();
    })
});