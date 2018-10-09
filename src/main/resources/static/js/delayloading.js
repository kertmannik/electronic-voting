$(document).ready(function () {
  setTimeout(function () {
    $('.load-delay').each(function () {
      var imagex = $(this);
      var imgOriginal = imagex.data('original');
      $(imagex).attr('src', imgOriginal);
//      $(imagex).attr("class","image-after");
    });
  }, 3000);
});