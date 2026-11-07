package com.innovacorp.reliant.platform.iam.application.queryservices;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.User;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetAllUsersByOrganizationIdQuery;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetUserByIdQuery;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {

    List<User> handle(GetAllUsersByOrganizationIdQuery query);

    Optional<User> handle(GetUserByIdQuery query);

    Optional<User> handle(GetUserByEmailQuery query);
}