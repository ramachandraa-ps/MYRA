package com.myrafriend.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myrafriend.repository.AuthRepository;
import com.myrafriend.repository.MessageRepository;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

/**
 * Message ViewModel
 * Manages UI state for messaging and appointment screens
 */
public class MessageViewModel extends AndroidViewModel {
    private final MessageRepository messageRepository;

    public MessageViewModel(@NonNull Application application) {
        super(application);
        messageRepository = new MessageRepository(application);
    }

    /**
     * Get messages
     */
    public LiveData<AuthRepository.Resource<List<Map<String, Object>>>> getMessages(int userId) {
        return messageRepository.getMessages(userId);
    }

    /**
     * Send message
     */
    public LiveData<AuthRepository.Resource<Void>> sendMessage(
            int receiverId, String message, MultipartBody.Part attachment) {
        return messageRepository.sendMessage(receiverId, message, attachment);
    }

    /**
     * Mark messages as read
     */
    public LiveData<AuthRepository.Resource<Void>> markMessagesAsRead(int userId) {
        return messageRepository.markMessagesAsRead(userId);
    }

    /**
     * Get appointments
     */
    public LiveData<AuthRepository.Resource<List<Map<String, Object>>>> getAppointments(int userId, String roleId) {
        return messageRepository.getAppointments(userId, roleId);
    }

    /**
     * Book appointment
     */
    public LiveData<AuthRepository.Resource<Void>> bookAppointment(
            int doctorId, String appointmentDate, String appointmentTime, String reason) {
        return messageRepository.bookAppointment(doctorId, appointmentDate, appointmentTime, reason);
    }

    /**
     * Cancel appointment
     */
    public LiveData<AuthRepository.Resource<Void>> cancelAppointment(int appointmentId) {
        return messageRepository.cancelAppointment(appointmentId);
    }
}
