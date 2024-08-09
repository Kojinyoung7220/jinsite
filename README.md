"# jinsite" 


## 게시글
POST/posts
GET /post/{postId}

## 댓글 -> 테이블 모델링(comment) (=Comment Entity)
1. POST /comments  => 이 경우에는 바디에다가 포스트id를 넣어줘야 할 수도 있다.
그 이유는 어떤 글에 댓글을 달지에 대해서는 주소에 드러나지 않기 때문이다.
혹은 쿼리 파라미터에 넣어야 된다.
{
   postId: 1
}
2. POST /comments?postId=1 
=> 이 경우엔 블로그만 운영할때는 상관없지만 만약 홈쇼핑까지 운영한다고 치면
이 댓글이 블로그 댓글인지 홈쇼핑 댓글인지 햇갈림. comments 기준으로 하기 때문에
===> json바디에다가 포스트 아이디를 넣는거 자체가 약간 찝찝하다 
{
   author: "답글자1"
   password: "1234"
   content: "잘 읽었습니다~~"
}

3. POST/posts/{postID}/commnets 
{
   author: "답글자1"
   password: "1234"
   content: "잘 읽었습니다~~"
} 이렇게 짜게 되면 삭제할 때 
DELETE /posts/{postId}/comments/{commentId}
이렇게 길게 만들어지게 된다. 나중에 짤때 알게 되겠지만
   commentId는 유니크한 값이기 때문에 /posts/{postId}
이 부분이 삭제가 되고 comments/{commentId} 이렇게 남게 된다.
또한 /posts/{postID}/commnets 이사이에 중간 레이어가 올 수도 있따.
   /posts/{postID}/category/{categoryId}/commnets
=> 그럼 컨트롤러나 서비스를 바꿔야 할 수도 있기 때문에 귀찮아진다.

최종으로는 
1. POST/posts/{postID}/commnets
2. DELETE /comments/{commentId}
3. PATCH /comments/{commentId}



## 비공개, 공개 여부 (상태값) -> (Enum)

## 카테고리 -> DB(or Enum)

## 로그인 -> spring security

## 비밀번호 암호화
1. 해시
2. 해시방식
    1. SHA1
    2. SHA256
    3. MD5
    4. 왜 이런걸로 비번 암호화 하면 안되는지
3. BCrypt,SCrypt, Argon2 ->왜 이걸로 암호화 해야하는지.
   1. salt 값
