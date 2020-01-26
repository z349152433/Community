//评论问题
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    commentById(questionId, 1, content);
}


function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    commentById(commentId, 2, content);
}


function commentById(id, type, content) {
    if (!content) {
        alert("评论内容不能为空");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": id,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("http://github.com/login/oauth/authorize?client_id=Iv1.cc8a97fc7f369230&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}


//弹出二级回复
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    if (comments.hasClass("in")) {
        comments.removeClass("in");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            comments.addClass("in");
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {

                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu",
                    }).append($("<span>", {
                            "class": "pull-right",
                            "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                        })
                    ));

                    var mediaElement = $("<div/>", {
                        "class": "media",
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12  comments ",
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });

                comments.addClass("in");
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }


    }

}

//
function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if(previous.indexOf(value) == -1){
        if(previous){
            $("#tag").val(previous+","+value);
        }else{
            $("#tag").val(value);
        }
    }


}

//
function showSelectTag() {
    $("#select-tag").show();
}