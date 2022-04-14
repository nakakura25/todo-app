// DOM読み込みが完了してから処理
document.addEventListener("DOMContentLoaded",function(){
  // 削除アイコンにonclickイベントを設定
  Array.from(
    document.getElementsByClassName("delete")
  ).forEach(action => {
    // eventを取得して、クリックされた要素(target)の親要素であるformをsubmitする
    action.addEventListener("click", (e) => {
      e.currentTarget.parentNode.submit();
      // 親要素にある詳細ページへのリンクを止める
      e.stopPropagation();
    });
  });

  // Cardのクリックで詳細ページへ飛ばす
  Array.from(
    document.getElementsByClassName("card")
  ).forEach(card => {
    card.addEventListener("click", (e) => {
      location.href = e.currentTarget.getAttribute("data-href");
    })
  })
});