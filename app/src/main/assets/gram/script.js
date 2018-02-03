'use strict';

function test(video,photo) {

if( window.location.href.indexOf("www.instagram.com/p/") != -1 ||  window.location.href.indexOf("www.instagram.com/v/") != -1 ||  window.location.href.indexOf("www.instagram.com/") != -1){
    $("._4rbun").each(function(){

                                      var button = document.createElement("button");
                                      button.setAttribute("type","button");
                                      button.setAttribute("id","save");
                                      button.setAttribute("class","downloadBtn");
                                      button.innerHTML = 'Download';





                  if($(this).parent().closest("._sxolz").find("#save").length != 0){

                  }else{

                         var blinks = $(this).find("img").attr("src");

                         $(button).data('src',blinks);
                         $(button).data('type',"photo");

                         if($(this).parent().closest("._sxolz").length != 0){
                          button.innerHTML = photo;
                         $(this).parent().closest("._sxolz").append(button);

                         }




                  }




    });



    $("._qzesf").each(function(){

                                 var button = document.createElement("button");
                                 button.setAttribute("type","button");
                                 button.setAttribute("id","save");
                                 button.setAttribute("class","downloadBtn");
                                 button.innerHTML = 'Download';




                              if($(this).parent().closest("._gxii9._usfov._fnlap").find("#save").length != 0){


                              }else{

                                  var links = $(this).find("video").attr("src");
                                  $(button).data('src',links);
                                  $(button).data('type',"video");

                                  if($(this).parent().closest("._gxii9._usfov._fnlap").length != 0){
                                   button.innerHTML = video;

                                    $(this).parent().closest('._gxii9._usfov._fnlap').append(button);




                                 }



                              }




    });



 $('.downloadBtn').on("touchend", function (e) {
   var blonde = $(this).data('src');
   var mime = $(this).data('type');

    e.preventDefault();
     window.JSInterface.startVideo(blonde,mime);
 });

 }



}



