package com.chsrvn.splitsoservice.controller;

import com.chsrvn.splitsoservice.entity.Group;
import com.chsrvn.splitsoservice.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createGroup(group));
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<Group> updateGroup(
            @PathVariable UUID groupId,
            @RequestBody Group groupDetails,
            @RequestParam UUID requestingUserId) {
        return ResponseEntity.ok(groupService.updateGroup(groupId, groupDetails, requestingUserId));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable UUID groupId,
            @RequestParam UUID requestingUserId) {
        groupService.deleteGroup(groupId, requestingUserId);
        return ResponseEntity.noContent().build();
    }
}