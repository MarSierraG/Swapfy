package com.swapfy.backend.services;

import com.swapfy.backend.models.Tag;
import com.swapfy.backend.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    // Obtener todas las etiquetas
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    // Obtener una etiqueta por su ID
    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id);
    }

    // Crear una nueva etiqueta
    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    // Actualizar una etiqueta existente
    public Tag updateTag(Long id, Tag tagDetails) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada"));
        tag.setName(tagDetails.getName());
        return tagRepository.save(tag);
    }

    // Eliminar una etiqueta
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada"));
        tagRepository.delete(tag);
    }

    // Obtener etiquetas predeterminadas
    public List<Tag> getDefaultTags() {
        // Si no hay etiquetas en la base de datos, devuelve las etiquetas predeterminadas
        List<Tag> defaultTags = Arrays.asList(
                new Tag("Bici"),
                new Tag("Ciclismo"),
                new Tag("Deporte")
        );

        for (Tag tag : defaultTags) {
            // Asegura que las etiquetas predeterminadas estén guardadas
            tagRepository.save(tag);
        }

        return defaultTags;
    }
}
