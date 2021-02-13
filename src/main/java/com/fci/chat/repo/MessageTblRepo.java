package com.fci.chat.repo;

import com.fci.chat.entity.MessageTbl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

public interface MessageTblRepo extends CrudRepository<MessageTbl,Integer> {
    public List<MessageTbl> findAllByUserIdAndMessageIdAfterOrderByTimestampDesc(String user, int messageId,Pageable pageable);
    public default int deleteLog(String user, Optional<Integer> optionalMessageId, EntityManager entityManager){
        CriteriaBuilder cb=entityManager.getCriteriaBuilder();
        CriteriaDelete<MessageTbl>cd=cb.createCriteriaDelete(MessageTbl.class);
        Root<MessageTbl> root=cd.from(MessageTbl.class);
        Predicate predicate=cb.equal(root.get("userId"),user);
        if(optionalMessageId.isPresent())
            predicate=cb.and(predicate,cb.equal(root.get("messageId"),optionalMessageId.get()));

        cd.where(predicate);

        Query query=entityManager.createQuery(cd);
        return query.executeUpdate();

    }
}
