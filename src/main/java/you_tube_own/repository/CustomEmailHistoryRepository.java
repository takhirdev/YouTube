package you_tube_own.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import you_tube_own.dto.email.EmailHistoryFilterDto;
import you_tube_own.dto.email.FilterResponseDto;
import you_tube_own.entity.EmailHistoryEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomEmailHistoryRepository {

    @Autowired
    private EntityManager entityManager;

    public FilterResponseDto<EmailHistoryEntity> filter(EmailHistoryFilterDto dto, int page, int size) {

        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        if (dto.getEmail() != null) {
            query.append(" and eh.email = :email ");
            params.put("email", dto.getEmail());
        }

        if (dto.getCreatedDateFrom() != null) {
            query.append(" and created_date >= :createdDateFrom ");
            params.put("createdDateFrom", dto.getCreatedDateFrom());
        }

        if (dto.getCreatedDateTo() != null) {
            query.append(" and created_date <= :createdDateTo ");
            params.put("createdDateTo", dto.getCreatedDateTo());
        }

        StringBuilder selectSQL = new StringBuilder(" from EmailHistoryEntity as eh where 1 = 1 ").append(query);
        StringBuilder countSQL = new StringBuilder(" select count(eh) from EmailHistoryEntity as eh where 1 = 1 ").append(query);

        Query selectQuery = entityManager.createQuery(selectSQL.toString());
        Query countQuery = entityManager.createQuery(countSQL.toString());

        params.forEach((key, value) -> {
            selectQuery.setParameter(key, value);
            countQuery.setParameter(key, value);
        });

        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);

        List<EmailHistoryEntity> list = selectQuery.getResultList();
        Long totalCount = (Long) countQuery.getSingleResult();

        return new FilterResponseDto<EmailHistoryEntity>(list,totalCount);
    }
}
