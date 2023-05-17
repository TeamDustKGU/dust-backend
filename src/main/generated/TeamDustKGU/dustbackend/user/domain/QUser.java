package TeamDustKGU.dustbackend.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -343690206L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final TeamDustKGU.dustbackend.global.QBaseTimeEntity _super = new TeamDustKGU.dustbackend.global.QBaseTimeEntity(this);

    public final NumberPath<Integer> auth = createNumber("auth", Integer.class);

    public final ListPath<TeamDustKGU.dustbackend.board.domain.Board, TeamDustKGU.dustbackend.board.domain.QBoard> boardList = this.<TeamDustKGU.dustbackend.board.domain.Board, TeamDustKGU.dustbackend.board.domain.QBoard>createList("boardList", TeamDustKGU.dustbackend.board.domain.Board.class, TeamDustKGU.dustbackend.board.domain.QBoard.class, PathInits.DIRECT2);

    public final ListPath<TeamDustKGU.dustbackend.comment.domain.Comment, TeamDustKGU.dustbackend.comment.domain.QComment> commentList = this.<TeamDustKGU.dustbackend.comment.domain.Comment, TeamDustKGU.dustbackend.comment.domain.QComment>createList("commentList", TeamDustKGU.dustbackend.comment.domain.Comment.class, TeamDustKGU.dustbackend.comment.domain.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QEmail email;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final QNickname nickname;

    public final StringPath password = createString("password");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.email = inits.isInitialized("email") ? new QEmail(forProperty("email")) : null;
        this.nickname = inits.isInitialized("nickname") ? new QNickname(forProperty("nickname")) : null;
    }

}

