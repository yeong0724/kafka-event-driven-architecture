# MongoDB

### database 생성 및 사용
```sh
use campus
```

이후, 아래 명령어를 통해 현재 그 db를 사용하도록 잘 설정되었는지 확인 가능하다.

```sh
db
```

### collection 생성
```sh
db.createCollection("subscribingInboxPosts")
```

### collection에 index 생성
```sh
db.subscribingInboxPosts.createIndex( { "followerUserId": -1, "postCreatedAt": -1 } )
db.subscribingInboxPosts.createIndex( { "postId": -1 } )
```

이후, 아래 명령어를 통해 현재 그 인덱스가 잘 생성되었는지 확인 가능하다.

```sh
db.subscribingInboxPosts.getIndexes()
```

### Document 예시
```json
{
    "_id": "12_2",
    "postId": 12,
    "followerUserId": 2,
    "postCreatedAt": "2023-12-25T13:22:58.070Z",
    "addedAt": "2023-12-25T13:24:50.010Z",
    "read": false
}
```

NoSQL 기반의 DB 여서 정규화 하지 않고 사용하는 것이 일반적이다.

다시말해 Join 할 필요 없는 데이터를 위해서 사용하고, 오히려 반대로 Join 할 필요있는 데이터를 Join 된 형태로 미리 만들어 저장하기도 한다.
따라서 2개 이상의 맥락을 함께 저장하려고 할 때 적합하다.

여기서 2개 이상의 맥락이라 함은, 2개 이상의 원천데이터의 내용을 함께 저장하려고 할 때 NoSQL 이 적합할 것이다.

추가로 구독을 받을 사람, 콘텐츠의 관계만을 저장할 것이 아니라 구독자에게 "해당 콘텐츠가 몇회 노출 되었는지"와 같은 통계적인 데이터를 함께 보관한다거나
기타 등등 구독 서비스에서는 함께 저장하고 싶은 맥락이 더 늘어날 수 있기 때문에 Schema 확장성이 좋은 NoSQL 인 MongoDB 가 적합할 것이다.

