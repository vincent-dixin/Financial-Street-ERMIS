
function overlib(obj){
}

function nd(){
}

/*********设置颜色的代码**************/
var CURRENT_COLOR = "#ff0000";
var HAS_DRAG_SET = "NO";
var COLOR_SET_ID = "";
var CURRENT_BIZCONCEPT = "0";
function pressSample(obj){
   
   f.style.display="block";
   f.style.pixelLeft = document.body.scrollLeft + event.clientX - 10;
   f.style.pixelTop = document.body.scrollTop + event.clientY -10;
   f.style.backgroundColor = obj.bc;
 
   COLOR_SET_ID = obj.ColorSetId;
   CURRENT_BIZCONCEPT = obj.BizConcept;
   
   BeforeDrag();
}

function releaseDiv(){
   EndDrag();
   f.style.display="none";
   CURRENT_COLOR = f.style.backgroundColor;
  
   HAS_DRAG_SET = "YES";
}

//设置颜色
function chanageColor(obj){
   if(HAS_DRAG_SET  == "YES"){
      obj.bc = CURRENT_COLOR;
      obj.style.backgroundColor = CURRENT_COLOR;
      HAS_DRAG_SET = "NO";
      obj.ColorSetId = COLOR_SET_ID;
      obj.BizConcept = CURRENT_BIZCONCEPT;
   }
}
/********************************/


