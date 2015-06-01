SyntaxHighlighter.defaults['smart-tabs'] = true;
SyntaxHighlighter.defaults['toolbar'] = false;
SyntaxHighlighter.defaults['ruler'] = true;
SyntaxHighlighter.defaults['class-name'] = "sourceCode";
SyntaxHighlighter.all();

$(document).ready(function(){
    /* 自动生成内容目录 */
    $('#mainContent > h1, h2, h3').each(function(i){
        var current = $(this);
        current.attr("id", "title" + i);
        
        $("#sidebar1").append("<a id='link" + i + "' href='#title" + 
				 i + "' title='" + current.attr("tagName") + "'>" + 
				 current.html() + "</a><br />");
    });
    
    /* 设置表格的表头，表体奇/偶数行格式 */
    $('#mainContent > table').each(function(i){
        var current = $(this);
        if (!current.hasClass('notice')) {
            $(this).find('tr').each(function(j){
                var current = $(this);
                if (j == 0) {
                    $(this).find('td').each(function(k){
                        var current = $(this);
                        current.addClass("tableHead");
                    });
                }
                else 
                    if (j % 2 == 0) {
                        current.addClass("odd");
                    }
                    else {
                        current.addClass("even");
                    }
            });
        }
    });
});
