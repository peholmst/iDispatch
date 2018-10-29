package net.pkhapps.idispatch.client.v3;

import retrofit2.Call;

import java.util.List;

public interface AssignmentTypeLookupService {

    Call<AssignmentType> findById(AssignmentTypeId id);

    Call<List<AssignmentType>> findAll();
}
