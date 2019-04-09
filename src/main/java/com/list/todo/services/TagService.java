package com.list.todo.services;

import com.list.todo.entity.Tag;
import com.list.todo.entity.TaggedTask;
import com.list.todo.payload.TagInput;
import com.list.todo.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    private final TaggedTaskService taggedTaskService;

    public Optional<Tag> getTagById(Long tagId) {
        return tagRepository.findById(tagId);
    }

    public Iterable<Tag> getTagsByOwnerId(Long ownerId) {
        return tagRepository.getByOwnerId(ownerId);
    }

    public Optional<TaggedTask> addTagToTask(Tag tag, Long taskId) {
        return taggedTaskService.addTaggedTask(new TaggedTask(taskId, tag));
    }

    public void removeTagFromTask(Long taskId, Tag tag) {
        taggedTaskService.deleteTaggedTask(taskId, tag);
    }

    public Optional<Tag> addTag(TagInput tagInput, Long currentUserId) {
        Tag newTag = Tag.builder()
                .tagName(tagInput.getTagName())
                .ownerId(currentUserId)
                .build();

        return Optional.of(tagRepository.save(newTag));
    }

    public Optional<Tag> updateTag(Long currentTagId, TagInput tagInput) {
        return tagRepository.findById(currentTagId)
                .map(tag -> {
                    tag.setTagName(tagInput.getTagName());
                    return tagRepository.save(tag);
                });
    }

    public void deleteTag(Long tagId) {
        Optional<Tag> tag = tagRepository.findById(tagId);

        tag.ifPresent(value -> taggedTaskService.getTaggedTasksByTag(value)
                .forEach(taggedTaskService::deleteTaggedTask));

        tagRepository.deleteById(tagId);
    }

}