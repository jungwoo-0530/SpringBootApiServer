Response

```java
@GetMapping("/test/qna")
  public List<PostDto> testPostList() {
    List<Post> list = postService.findAll();
    List<PostDto> result = new ArrayList<>();
    for (Post a : list) {
      PostDto postDto = PostDto.builder().
          title(a.getTitle()).
          content(a.getContent()).build();
      result.add(postDto);
    }
    return result;
  }
```

<img src="/Users/jungwoo/Library/Application Support/typora-user-images/image-20220211002541903.png" alt="image-20220211002541903" style="zoom:50%;" />



```java
@GetMapping("/test/qna")
  public ResponseEntity<List<PostDto>> testPostList() {
    List<Post> list = postService.findAll();
    List<PostDto> result = new ArrayList<>();
    for (Post a : list) {
      PostDto postDto = PostDto.builder().
          title(a.getTitle()).
          content(a.getContent()).build();
      result.add(postDto);
    }
    return ResponseEntity.ok().body(result);
  }
```

<img src="/Users/jungwoo/Library/Application Support/typora-user-images/image-20220211002754091.png" alt="image-20220211002754091" style="zoom:50%;" />





```java
@GetMapping("/test/qna")
  public ResponseEntity<? extends BasicResponse> testPostList() {
    List<Post> list = postService.findAll();
    List<PostDto> result = new ArrayList<>();
    for (Post a : list) {
      PostDto postDto = PostDto.builder().
          title(a.getTitle()).
          content(a.getContent()).build();
      result.add(postDto);
    }
    return ResponseEntity.ok().body(new CommonResponse<>(result));
  }
```

```java
@Getter
@Setter
public class CommonResponse<T> extends BasicResponse{

  private int count;
  private T data;

  public CommonResponse(T data) {

    this.data = data;
    if(data instanceof List){
      this.count = ((List<?>)data).size();
    }else{
      this.count = 1;
    }

  }

}
```

<img src="/Users/jungwoo/Library/Application Support/typora-user-images/image-20220211003021744.png" alt="image-20220211003021744" style="zoom:50%;" />







