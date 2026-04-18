package com.chsrvn.splitsoservice.controller;

import com.chsrvn.splitsoservice.entity.Group;
import com.chsrvn.splitsoservice.entity.User;
import com.chsrvn.splitsoservice.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping()
    public ResponseEntity<List<Group>> getGroupsForUser(Authentication authentication) {
        UUID userId = getUserId(authentication);
        List<Group> groups = groupService.getGroupsForUser(userId);

        if (groups.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(groups);
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createGroup(group));
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<Group> updateGroup(
            @PathVariable UUID groupId,
            @RequestBody Group groupDetails,
            Authentication authentication) {
        UUID userId = getUserId(authentication);
        return ResponseEntity.ok(groupService.updateGroup(groupId, groupDetails, userId));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable UUID groupId,
            Authentication authentication) {
        UUID userId = getUserId(authentication);
        groupService.deleteGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }


    private UUID getUserId(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        assert principal != null;
        return principal.getId();
    }
}