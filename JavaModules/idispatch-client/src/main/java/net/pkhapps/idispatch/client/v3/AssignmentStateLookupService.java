package net.pkhapps.idispatch.client.v3;

import retrofit2.Call;

import java.util.List;

public interface AssignmentStateLookupService {

    Call<AssignmentState> findById(AssignmentStateId id);

    Call<List<AssignmentState>> findAll();
}
