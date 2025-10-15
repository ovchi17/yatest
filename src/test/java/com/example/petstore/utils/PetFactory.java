package com.example.petstore.utils;

import com.example.petstore.models.Pet;
import java.util.List;
import java.util.Random;

public class PetFactory {

    static Random random = new Random();

    public static Pet createRandomPet(Pet.Status status) {
        Pet pet = new Pet();
        pet.setId(System.currentTimeMillis());
        Pet.Category category = new Pet.Category();
        category.setId(System.currentTimeMillis() % 10000 + random.nextInt(10000));
        category.setName("Pet category" + category.getId());
        pet.setCategory(category);
        pet.setName("Pet number" + pet.getId());
        pet.setPhotoUrls(List.of("https://www.petphoto.com/" + pet.getId()));
        Pet.Tag tag = new Pet.Tag();
        tag.setId(System.currentTimeMillis() % 20000 + random.nextInt(10000));
        tag.setName("Pet tag" + tag.getId());
        pet.setTags(List.of(tag));
        pet.setStatus(status);
        return pet;
    }

    public static Pet createRandomUpdatedPet(Pet createdPet, Pet.Status status) {
        Pet toUpdatePet = new Pet();
        toUpdatePet.setId(createdPet.getId());
        Pet.Category category = new Pet.Category();
        category.setId(System.currentTimeMillis() % 20000 + random.nextInt(10000));
        category.setName("Updated pet category" + category.getId());
        toUpdatePet.setCategory(category);
        toUpdatePet.setName("nameUpdate");
        toUpdatePet.setPhotoUrls(List.of("https://www.petupdatedphoto.com/" + toUpdatePet.getId()));
        Pet.Tag tag = new Pet.Tag();
        tag.setId(System.currentTimeMillis() % 50000 + random.nextInt(10000));
        tag.setName("Updated pet tag" + tag.getId());
        toUpdatePet.setTags(List.of(tag));
        toUpdatePet.setStatus(status);
        return toUpdatePet;
    }

    public static Pet.FormDataForUpdateById createRandomPetFormDataForUpdateById(Pet.Status status) {
        Pet.FormDataForUpdateById formData = new Pet.FormDataForUpdateById();
        formData.setName("Pet updated name");
        formData.setStatus(status);
        return formData;
    }
}
