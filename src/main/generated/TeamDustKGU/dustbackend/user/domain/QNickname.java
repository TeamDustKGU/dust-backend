package TeamDustKGU.dustbackend.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.querydsl.core.types.dsl.*;
import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

/**
 * QNickname is a Querydsl query type for Nickname
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QNickname extends BeanPath<Nickname> {
    private static final long serialVersionUID = 1890151749L;

    public static final QNickname nickname = new QNickname("nickname");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath value = createString("value");

    public QNickname(String variable) {
        super(Nickname.class, forVariable(variable));
    }

    public QNickname(Path<? extends Nickname> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNickname(PathMetadata metadata) { super(Nickname.class, metadata);}
}

